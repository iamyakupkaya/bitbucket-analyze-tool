package com.orion.bitbucket.Bitbucket.service;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.orion.bitbucket.Bitbucket.dbc.TransactionManager;
import com.orion.bitbucket.Bitbucket.model.BranchDO;
import com.orion.bitbucket.Bitbucket.model.PullRequestDO;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.*;
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

    private final String SQL_INSERT_PULL_REQUEST = "insert into pullrequest (id, title, state, closed, description, update_date, created_date, closed_date, email_address, display_name, slug) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    private final String SQL_INSERT_REVIEW = "insert into review (reviewer_id , display_name, email_address, approved, status) values (?, ?, ?, ?, ?)";
    private final String SQL_INSERT_PULL_REQUEST_REVIEW_RELATION = "insert into PullRequestReviewRelation (pull_request_id, review_id) values (?, ?)";
    private final String SQL_SELECT_COUNT_PULL_REQUEST = "select count(*) from pullrequest;";
    private final String SQL_SELECT_COUNT_REVIEW = "select count(*) from review";
    private final String SQL_GET_REVIEW_ID_BY_USERNAME = "select id from review where display_name=? order by id desc limit 1;";

    public void getData() {
        try {
            if (isPullRequestTableEmpty()) {
                Instant start = Instant.now();
                System.out.println(
                        "This is first time to retrieve data. Wait for retrieving data and insert into local database have been completed.");
                getPullRequestData(BitbucketConstants.EndPoints.OPEN_PRS);
               // getPullRequestData(BitbucketConstants.EndPoints.DECLINED_PRS);
               // getPullRequestData(BitbucketConstants.EndPoints.MERGED_PRS);
                Instant finish = Instant.now();
                Duration timeElapsed = Duration.between(start, finish);
                System.out.println(
                        "Response time to retrieve all merge, open and declined PRs: " + timeElapsed.toSeconds()
                                + " seconds.");
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public boolean isPullRequestTableEmpty() {
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
        int pullRequestId = (int) object.get("id");
        String title = (String) object.get("title");
        String state = (String) object.get("state");
        boolean closed = (boolean) object.get("closed");
        String description = object.optString("description");
        long updatedDate = (long) object.get("updatedDate");
        Long createdDate = (long) object.get("createdDate");
        Long closedDate = object.optLong("closedDate");
        // Author Information
        JSONObject author = object.getJSONObject("author");
        JSONObject user = author.getJSONObject("user");
        String emailAddress = user.optString("emailAddress");
        String displayName = (String) user.get("displayName");
        String slug = (String) user.get("slug");
        // Review Information
        JSONArray reviewers = object.getJSONArray("reviewers");
        for (int i = 0; i < reviewers.length(); i++) {
            JSONObject reviewer = reviewers.getJSONObject(i);
            user = reviewer.getJSONObject("user");
            int id = (int) user.get("id");
            String reviewerDisplayName = (String) user.get("displayName");
            String reviewerEmailAddress = user.optString("emailAddress");
            String reviewStatus = reviewer.optString("status");
            boolean reviewerApproved = reviewer.optBoolean("approved");
            insertReview(id, reviewerDisplayName, reviewerEmailAddress, reviewerApproved, reviewStatus);
            int reviewId = getReviewIdByUsername(reviewerDisplayName);
            insertPullRequestReviewRelation(pullRequestId, reviewId);
        }
        insertPullRequest(pullRequestId, title, state, closed, description, updatedDate, createdDate, closedDate,
                emailAddress, displayName, slug);
    }

    public int getReviewIdByUsername(String username) throws SQLException {
        Connection connection = TransactionManager.getConnection();
        int id = 0;
        PreparedStatement preparedStmt = null;
        preparedStmt = connection.prepareStatement(SQL_GET_REVIEW_ID_BY_USERNAME);
        preparedStmt.setString(1, username);
        ResultSet resultSet = preparedStmt.executeQuery();
        connection.commit();
        while (resultSet.next()) {
            id = resultSet.getInt("id");
        }
        resultSet.close();
        preparedStmt.close();
        connection.close();
        return id;
    }

    public void insertPullRequest(int prId, String title, String state, boolean closed, String description,
            long updatedDate, Long createdDate, Long closedDate, String emailAddress, String displayName,
            String slug) throws SQLException {
        Connection connection = TransactionManager.getConnection();

        java.sql.Date sqlPackageDateCreated = new java.sql.Date(createdDate);

        java.sql.Date sqlPackageDateClosed = new java.sql.Date(closedDate);

        try {
            PreparedStatement preparedStmt = null;
            preparedStmt = connection.prepareStatement(SQL_INSERT_PULL_REQUEST);
            preparedStmt.setInt(1, prId);
            preparedStmt.setString(2, title);
            preparedStmt.setString(3, state);
            preparedStmt.setBoolean(4, closed);
            preparedStmt.setString(5, description);
            preparedStmt.setLong(6, updatedDate);
            preparedStmt.setDate(7, sqlPackageDateCreated);
            preparedStmt.setDate(8, sqlPackageDateClosed);
            preparedStmt.setString(9, emailAddress);
            preparedStmt.setString(10, displayName);
            preparedStmt.setString(11, slug);
            int row = preparedStmt.executeUpdate();
            connection.commit();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        } finally {
            connection.close();
        }
    }

    public void insertReview(int id, String reviewerDisplayName, String reviewerEmailAddress, boolean reviewerApproved,
            String reviewStatus) throws SQLException {
        Connection connection = TransactionManager.getConnection();
        try {
            PreparedStatement preparedStmt = null;
            preparedStmt = connection.prepareStatement(SQL_INSERT_REVIEW);
            preparedStmt.setInt(1, id);
            preparedStmt.setString(2, reviewerDisplayName);
            preparedStmt.setString(3, reviewerEmailAddress);
            preparedStmt.setBoolean(4, reviewerApproved);
            preparedStmt.setString(5, reviewStatus);
            int row = preparedStmt.executeUpdate();
            connection.commit();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        } finally {
            connection.close();
        }
    }

    public void insertPullRequestReviewRelation(int prId, int reviewerId) throws SQLException {
        Connection connection = TransactionManager.getConnection();
        try {
            PreparedStatement preparedStmt = null;
            preparedStmt = connection.prepareStatement(SQL_INSERT_PULL_REQUEST_REVIEW_RELATION);
            preparedStmt.setInt(1, prId);
            preparedStmt.setInt(2, reviewerId);
            int row = preparedStmt.executeUpdate();
            connection.commit();
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

}