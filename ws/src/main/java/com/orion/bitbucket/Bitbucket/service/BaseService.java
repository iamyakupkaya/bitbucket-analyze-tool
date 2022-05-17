package com.orion.bitbucket.Bitbucket.service;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.orion.bitbucket.Bitbucket.dbc.DBConstants;
import com.orion.bitbucket.Bitbucket.dbc.DatabaseManager;
import com.orion.bitbucket.Bitbucket.dbc.TransactionManager;
import com.orion.bitbucket.Bitbucket.model.BranchDO;
import com.orion.bitbucket.Bitbucket.model.PullRequestDO;
import com.orion.bitbucket.Bitbucket.model.ReviewDO;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;

@Service
public class BaseService implements BaseServiceIF {

    @Autowired
    private JsonResponse response;
    static ArrayList<PullRequestDO> openPRList;
    static ArrayList<PullRequestDO> mergedPRList;
    static ArrayList<PullRequestDO> declinedPRList;
    static ArrayList<PullRequestDO> allPRList;


    private boolean isDebug = false;

    private final String SQL_INSERT_PULL_REQUEST = "INSERT INTO pullrequest (id, title, state, closed, description, update_date, created_date, closed_date, email_address, display_name, slug) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    private final String SQL_SELECT_COUNT_PULL_REQUEST = "SELECT COUNT(*) FROM PULLREQUEST;";

    public void getData() {
        try {
            if (isFirstTime()) {
                Instant start = Instant.now();
                getPullRequestData(BitbucketConstants.EndPoints.OPEN_PRS);
                getPullRequestData(BitbucketConstants.EndPoints.DECLINED_PRS);
                getPullRequestData(BitbucketConstants.EndPoints.MERGED_PRS);
                Instant finish = Instant.now();
                Duration timeElapsed = Duration.between(start, finish);
                System.out.println("Response time to retrieve all merge, open and declined PRs: " + timeElapsed.toSeconds()
                        + " seconds.");
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public boolean isFirstTime() {
        Connection connection = TransactionManager.getConnection();
        int count = 0;
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(SQL_SELECT_COUNT_PULL_REQUEST);
            while (resultSet.next()) {
                count = resultSet.getInt("count");
            }
            resultSet.close();
            statement.close();
            connection.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        return count > 0 ? false : true;
    }

    public void createAllPRList() {
        this.allPRList = new ArrayList<PullRequestDO>();
        allPRList.addAll(this.openPRList);
        allPRList.addAll(this.mergedPRList);
        allPRList.addAll(this.declinedPRList);
    }

    public void getPullRequestData(String url) throws UnirestException, JSONException, SQLException {

        int start = 0;
        boolean isLastPage = false;
        while (!isLastPage) {
            HttpResponse<JsonNode> httpResponse = response.getResponse(url + start, BitbucketConstants.Bearer.TOKEN);
            JSONObject body = httpResponse.getBody().getObject();
            Object values = body.get("values");
            JSONArray array = (JSONArray) values;
            for (int i = 0; i < array.length(); i++) {
                commonPullRequestDataParser(array.getJSONObject(i));
            }
            isLastPage = (boolean) body.get("isLastPage");
            start += 100;
        }
       

    }
    public void commonPullRequestDataParser(JSONObject object) throws SQLException {
        int prId = (int) object.get("id");
        String title = (String) object.get("title");
        String state = (String) object.get("state");
        boolean closed = (boolean) object.get("closed");
        String description = object.optString("description");
        long updatedDate = (long) object.get("updatedDate");
        String createdDate = convertDate((long) object.get("createdDate"));
        String closedDate = convertDate(object.optLong("closedDate"));

        // Author Information
        JSONObject author = object.getJSONObject("author");
        JSONObject user = author.getJSONObject("user");
        String emailAddress = user.optString("emailAddress");
        String displayName = (String) user.get("displayName");
        String slug = (String) user.get("slug");

        // Reviewer Information
        ArrayList<ReviewDO> reviewerList = new ArrayList<ReviewDO>();
        JSONArray reviewers = object.getJSONArray("reviewers");
        for (int i = 0; i < reviewers.length(); i++) {
            JSONObject reviewer = reviewers.getJSONObject(i);
            user = reviewer.getJSONObject("user");
            int id = (int) user.get("id");
            String reviewerDisplayName = (String) user.get("displayName");
            String reviewerEmailAddress = user.optString("emailAddress");
            String reviewStatus = reviewer.optString("status");
            boolean reviewerApproved = reviewer.optBoolean("approved");
            reviewerList
                    .add(new ReviewDO(id, reviewerDisplayName, reviewerEmailAddress, reviewStatus, reviewerApproved));
        }
        insertPullRequest(prId, title, state, closed, description, updatedDate,
                createdDate, closedDate, emailAddress, displayName, slug);
    }

    public void insertPullRequest(int prId, String title, String state, boolean closed, String description,
            long updatedDate, String createdDate, String closedDate, String emailAddress, String displayName,
            String slug) throws SQLException {
        Connection connection = null;
        Statement statement = null;
        PreparedStatement preparedStmt = null;
        try {
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection(DBConstants.getConnectionURL(), DBConstants.getDBUsername(),
                    DBConstants.getDBPassword());
            connection.setAutoCommit(false);
            if (isDebug) {
                System.out.println("Opened database successfully");
            }


            preparedStmt = connection.prepareStatement(SQL_INSERT_PULL_REQUEST);
            preparedStmt.setInt(1, prId);
            preparedStmt.setString(2, title);
            preparedStmt.setString(3, state);
            preparedStmt.setBoolean(4, closed);
            preparedStmt.setString(5, description);
            preparedStmt.setLong(6, updatedDate);
            preparedStmt.setString(7, createdDate);
            preparedStmt.setString(8, closedDate);
            preparedStmt.setString(9, emailAddress);
            preparedStmt.setString(10, displayName);
            preparedStmt.setString(11, slug);
            int row = preparedStmt.executeUpdate();

            connection.commit(); // ADDED

        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        } finally {
            connection.close();
        }
    }

    // TODO Branch url will be added onto constants, then we can call it while
    // getting data
    public ArrayList<BranchDO> getBranchData(String url) throws UnirestException {
        ArrayList<BranchDO> list = new ArrayList<BranchDO>();

        int start = 0;
        boolean isLastPage = false;
        while (!isLastPage) {
            HttpResponse<JsonNode> httpResponse = response.getResponse(url + start, BitbucketConstants.Bearer.TOKEN);
            JSONObject body = httpResponse.getBody().getObject();
            Object values = body.get("values");
            JSONArray array = (JSONArray) values;
            for (int i = 0; i < array.length(); i++) {
                // list.add(commonBranchDataParser(array.getJSONObject(i)));
            }
            isLastPage = (boolean) body.get("isLastPage");
            start += 100;
        }
        return list;
    }

    // TODO Below method will be modified for branch data
    public void commonBranchDataParser(JSONObject object) {
    }

    private String convertDate(Long date) {
        String pattern = "dd.MM.yyyy";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        String newDate = simpleDateFormat.format(date);
        return newDate;
    }
}