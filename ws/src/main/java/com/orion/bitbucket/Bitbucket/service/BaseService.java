package com.orion.bitbucket.Bitbucket.service;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.orion.bitbucket.Bitbucket.dbc.DBConstants;
import com.orion.bitbucket.Bitbucket.dbc.TransactionManager;
import com.orion.bitbucket.Bitbucket.model.BranchDO;
import com.orion.bitbucket.Bitbucket.model.PullRequestDO;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.orion.bitbucket.Bitbucket.log.Log;
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

    private final boolean IS_BASE_LOGGING = false;

    private final String SQL_INSERT_PULL_REQUEST = "insert into pullrequest (id, title, state, closed, description, update_date, created_date, closed_date, email_address, display_name, slug) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    private final String SQL_INSERT_REVIEW = "insert into review (reviewer_id , display_name, email_address, approved, status) values (?, ?, ?, ?, ?)";
    private final String SQL_INSERT_PULL_REQUEST_REVIEW_RELATION = "insert into PullRequestReviewRelation (pull_request_id, review_id) values (?, ?)";
    private final String SQL_SELECT_COUNT_PULL_REQUEST = "select count(*) from pullrequest;";
    private final String SQL_SELECT_COUNT_REVIEW = "select count(*) from review";
    private final String SQL_GET_REVIEW_ID_BY_USERNAME = "select id from review where display_name=? order by id desc limit 1;";
    private final String SQL_GET_REVIEW_PULL_REQUEST_DELETE_OLD_PR = "delete from pullrequest where id = ?";
    private final String SQL_GET_PULL_REQUEST_REVIEW_RELATION_DELETE = "delete from pullrequestreviewrelation where pull_request_id = ?";
    private final String SQL_GET_PULL_REQUEST_LAST_DATE_BY_STATE = "select closed_date from pullrequest where state = ? order by closed_date desc limit 1";
    private final String SQL_GET_PULL_REQUEST_LAST_DATE_WITH_CREATED_DATE = "select created_date from pullrequest where state = ? order by created_date desc limit 1";
    private final String SQL_GET_PULL_REQUEST_FIND_REVIEW_ID = "select review.id from review inner join pullrequestreviewrelation on review.id = review_id where pull_request_id = ?";
    private final String SQL_GET_PULL_REQUEST_REVIEW_ID_DELETE = "delete from review where id = ?;";
    public void getData() {

        if(IS_BASE_LOGGING) {
            try {
                Log.logger(Log.LogConstant.TAG_INFO, "entered in getData");
                if (isPullRequestTableEmpty()) {
                    Log.logger(Log.LogConstant.TAG_INFO, "isPullRequestTable is empty  : " + isPullRequestTableEmpty());
                    Instant start = Instant.now();
                    Log.logger(Log.LogConstant.TAG_INFO, "Instant started");
                    System.out.println(
                            "This is first time to retrieve data. Wait for retrieving data and insert into local database have been completed.");
                    getPullRequestData(BitbucketConstants.EndPoints.OPEN_PRS);
                    Log.logger(Log.LogConstant.TAG_INFO, "Success EndPoints.Open_PRS");
                    getPullRequestData(BitbucketConstants.EndPoints.DECLINED_PRS);
                    Log.logger(Log.LogConstant.TAG_INFO, "Success EndPoints.Declined_PRS");
                    getPullRequestData(BitbucketConstants.EndPoints.MERGED_PRS);
                    Log.logger(Log.LogConstant.TAG_INFO, "Success EndPoints.Merged_PRS");
                    Instant finish = Instant.now();
                    Log.logger(Log.LogConstant.TAG_INFO, "Instant finished");
                    Duration timeElapsed = Duration.between(start, finish);
                    Log.logger(Log.LogConstant.TAG_INFO, "timeElapsed  : " + timeElapsed.toSeconds());
                    System.out.println(
                            "Response time to retrieve all merge, open and declined PRs: " + timeElapsed.toSeconds()
                                    + " seconds.");
                }
            } catch (Exception e) {
                if(IS_BASE_LOGGING){
                    Log.logger(Log.LogConstant.TAG_ERROR, "isPullRequestTable not empty");
                }
            }
            Log.logger(Log.LogConstant.TAG_INFO,"no entrance getData");
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
        try{
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
        }catch (Exception e){
            Log.logger(Log.LogConstant.TAG_INFO,"Exception handling on getPullRequestData : " + e);
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
    public void updatePullRequest(){
        getUpdateMergedPullRequestData();
        getUpdateOpenPullRequestDate();
        getUpdateDeclinedPullRequestDate();
    }
    public void getUpdateMergedPullRequestData() throws  JSONException{

        int start = 0;
        boolean isLastPage = false;
        try{
            while (!isLastPage) {
                HttpResponse<JsonNode> httpResponse = response.getResponse(BitbucketConstants.EndPoints.ALL_PRS_DAILY_MERGED_UPDATE + start, BitbucketConstants.Bearer.TOKEN);
                JSONObject body = httpResponse.getBody().getObject();
                Object values = body.get("values");
                JSONArray array = (JSONArray) values;
                for (int i = 0; i < array.length(); i++) {
                    JSONObject object = array.getJSONObject(i);
                    int pullRequestId = (int) object.get("id");

                    Long pullRequestClosedDate = (long) object.get("closedDate");
                    java.sql.Date sqlPackageDateClosed = new java.sql.Date(pullRequestClosedDate);
                    Date lastDayInDatabase =  pullRequestLastDAY(DBConstants.PullRequestState.MERGED);
                    deleteOldPullRequest(pullRequestId);
                    deleteOldPullRequestReviewId(pullRequestId);
                    deleteOldPullRequestReviewRelation(pullRequestId);
                    if(IS_BASE_LOGGING){Log.logger(Log.LogConstant.TAG_INFO,
                            "Pull Request merged update date " + sqlPackageDateClosed +" ID : "+ pullRequestId);}
                    commonPullRequestDataParser(array.getJSONObject(i));

                    if(lastDayInDatabase.after(sqlPackageDateClosed)){
                        return;}
                }
                isLastPage = (boolean) body.get("isLastPage");
                start += 1;
            }
        }catch (Exception e){
            Log.logger(Log.LogConstant.TAG_INFO,"Exception handling on getPullRequestData : " + e);
        }
    }
    public void getUpdateOpenPullRequestDate() throws  JSONException{
        int start = 0;
        boolean isLastPage = false;
        try{
            while (!isLastPage) {
                HttpResponse<JsonNode> httpResponse = response.getResponse(BitbucketConstants.EndPoints.ALL_PRS_DAILY_OPEN_UPDATE + start, BitbucketConstants.Bearer.TOKEN);
                JSONObject body = httpResponse.getBody().getObject();
                Object values = body.get("values");
                JSONArray array = (JSONArray) values;
                for (int i = 0; i < array.length(); i++) {
                    JSONObject object = array.getJSONObject(i);
                    int pullRequestId = (int) object.get("id");

                    Long pullRequestCreatedDate = (long) object.get("createdDate");
                    java.sql.Date sqlPackageDateCreated = new java.sql.Date(pullRequestCreatedDate);
                    Date lastDayInDatabase =  pullRequestLastDayWithCreatedDate(DBConstants.PullRequestState.OPEN);
                    deleteOldPullRequest(pullRequestId);
                    deleteOldPullRequestReviewId(pullRequestId);
                    deleteOldPullRequestReviewRelation(pullRequestId);
                    if(IS_BASE_LOGGING){Log.logger(Log.LogConstant.TAG_INFO,
                            "Pull Request open update date " + sqlPackageDateCreated +" ID : "+ pullRequestId);}
                    commonPullRequestDataParser(array.getJSONObject(i));

                    if(lastDayInDatabase.after(sqlPackageDateCreated)){
                        return;}
                }
                isLastPage = (boolean) body.get("isLastPage");
                start += 1;
            }
        }catch (Exception e){
            Log.logger(Log.LogConstant.TAG_INFO,"Exception handling on getPullRequestData : " + e);
        }
    }
    public void getUpdateDeclinedPullRequestDate() throws  JSONException{
        int start = 0;
        boolean isLastPage = false;
        try{
            while (!isLastPage) {
                HttpResponse<JsonNode> httpResponse = response.getResponse(BitbucketConstants.EndPoints.ALL_PRS_DAILY_DECLINED_UPDATE + start, BitbucketConstants.Bearer.TOKEN);
                JSONObject body = httpResponse.getBody().getObject();
                Object values = body.get("values");
                JSONArray array = (JSONArray) values;
                for (int i = 0; i < array.length(); i++) {
                    JSONObject object = array.getJSONObject(i);
                    int pullRequestId = (int) object.get("id");

                    Long pullRequestClosedDate = (long) object.get("closedDate");
                    java.sql.Date sqlPackageDateClosed = new java.sql.Date(pullRequestClosedDate);
                    Date lastDayInDatabase =  pullRequestLastDAY(DBConstants.PullRequestState.DECLINED);
                    deleteOldPullRequest(pullRequestId);
                    deleteOldPullRequestReviewId(pullRequestId);
                    deleteOldPullRequestReviewRelation(pullRequestId);
                    if(IS_BASE_LOGGING){Log.logger(Log.LogConstant.TAG_INFO,
                            "Pull Request declined update date " + sqlPackageDateClosed +" ID : "+ pullRequestId);}
                    commonPullRequestDataParser(array.getJSONObject(i));

                    if(lastDayInDatabase.after(sqlPackageDateClosed)){
                        return;}
                }
                isLastPage = (boolean) body.get("isLastPage");
                start += 1;
            }
        }catch (Exception e){
            Log.logger(Log.LogConstant.TAG_INFO,"Exception handling on getPullRequestData : " + e);
        }
    }
    public void deleteOldPullRequest(int pullRequestId) throws SQLException {

        Connection connection = TransactionManager.getConnection();
        PreparedStatement preparedStatementForDelete = null;
        preparedStatementForDelete = connection.prepareStatement(SQL_GET_REVIEW_PULL_REQUEST_DELETE_OLD_PR);
        preparedStatementForDelete.setInt(1,pullRequestId);
        preparedStatementForDelete.executeUpdate();
        connection.commit();
        preparedStatementForDelete.close();
        connection.close();
    }
    public void deleteOldPullRequestReviewRelation(int pullRequestId) throws SQLException{
        Connection connection = TransactionManager.getConnection();
        PreparedStatement preparedStatement = null;
        preparedStatement = connection.prepareStatement(SQL_GET_PULL_REQUEST_REVIEW_RELATION_DELETE);
        preparedStatement.setInt(1,pullRequestId);
        preparedStatement.executeUpdate();
        connection.commit();
        preparedStatement.close();
        connection.close();
    }
    public void deleteOldPullRequestReviewId(int pullRequestId)throws SQLException{
        ArrayList<Integer> reviewId =  getPullRequestFindReviewId(pullRequestId);

        Connection connection = TransactionManager.getConnection();
        PreparedStatement preparedStatement = null;
        preparedStatement = connection.prepareStatement(SQL_GET_PULL_REQUEST_REVIEW_ID_DELETE);

        for (int i = 0; i<reviewId.size();i++){
            preparedStatement.setInt(1,reviewId.get(i));
            preparedStatement.executeUpdate();
            connection.commit();
        }
        preparedStatement.close();
        connection.close();
    }
    public ArrayList<Integer> getPullRequestFindReviewId (int pullRequestId) throws SQLException{

        ArrayList<Integer> reviewIdList = new ArrayList<Integer>();
        Connection connection = TransactionManager.getConnection();
        PreparedStatement preparedStatement = null;
        preparedStatement = connection.prepareStatement(SQL_GET_PULL_REQUEST_FIND_REVIEW_ID);
        preparedStatement.setInt(1,pullRequestId);
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()){
            reviewIdList.add(resultSet.getInt(DBConstants.PullRequest.PULL_REQUEST_ID));
        }
        resultSet.close();
        preparedStatement.close();
        connection.close();
        return reviewIdList;
    }
    public Date pullRequestLastDAY(String state) throws SQLException {

        Connection connection = TransactionManager.getConnection();
        PreparedStatement preparedStatement = null;
        preparedStatement = connection.prepareStatement(SQL_GET_PULL_REQUEST_LAST_DATE_BY_STATE);
        preparedStatement.setString(1,state);
        ResultSet resultSet = preparedStatement.executeQuery();
        Date date = null;
        while (resultSet.next()){
            date = resultSet.getDate(DBConstants.PullRequest.PULL_REQUEST_CLOSED_DATE);}
        resultSet.close();
        preparedStatement.close();
        connection.close();
        return date;
    }
    public Date pullRequestLastDayWithCreatedDate(String state) throws SQLException {

        Connection connection = TransactionManager.getConnection();
        PreparedStatement preparedStatement = null;
        preparedStatement = connection.prepareStatement(SQL_GET_PULL_REQUEST_LAST_DATE_WITH_CREATED_DATE);
        preparedStatement.setString(1,state);
        ResultSet resultSet = preparedStatement.executeQuery();
        Date date = null;
        while (resultSet.next()){
            date = resultSet.getDate(DBConstants.PullRequest.PULL_REQUEST_CREATED_DATE);}
        resultSet.close();
        preparedStatement.close();
        connection.close();
        return date;
    }

}