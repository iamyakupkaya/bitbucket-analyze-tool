package com.orion.bitbucket.Bitbucket.service;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.orion.bitbucket.Bitbucket.dbc.DBConstants;
import com.orion.bitbucket.Bitbucket.dbc.TransactionManager;
import com.orion.bitbucket.Bitbucket.log.Log;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.sql.*;
import java.util.ArrayList;

@Service
public class UpdateService implements UpdateServiceIF{
    @Autowired
    private JsonResponse response;
    @Autowired
    private BaseService baseService;

    private final boolean IS_UPDATE_LOGGING = false;
    private boolean IS_UPDATE = false;
    private Date requestUpdateDate = null;
    private final ReviewerService reviewerService = new ReviewerService();
    private final String SQL_GET_REVIEW_PULL_REQUEST_DELETE_OLD_PR = "delete from pullrequest where id = ?";
    private final String SQL_GET_PULL_REQUEST_REVIEW_RELATION_DELETE = "delete from pullrequestreviewrelation where pull_request_id = ?";
    private final String SQL_GET_PULL_REQUEST_REVIEW_ID_DELETE = "delete from review where id = ?;";
    private final String SQL_GET_PULL_REQUEST_FIND_REVIEW_ID = "select review.id from review inner join pullrequestreviewrelation on review.id = review_id where pull_request_id = ?";
    private final String SQL_TRUNCATE_REVIEWER_TABLE = "truncate table reviewer";
    private final String SQL_GET_CHECK_AUTHOR = "select count(*) from author where name = ?";
    private final String SQL_GET_CHECK_PULL_REQUEST_ID = "select count(*) from pullrequest where id=?;";
    private final String SQL_GET_PULL_REQUEST_VERSION = "select version from pullrequest where id =?;";

    public void runUpdate(){
        getUpdateAllPullRequestData();
        if(IS_UPDATE){
            try {
                truncateReviewerTable();
                reviewerService.getAllReviewer();
            }catch (Exception exception){
                if (IS_UPDATE_LOGGING) {
                    Log.logger(Log.LogConstant.TAG_WARN, String.valueOf(exception));}
            }
        }
        IS_UPDATE = false;
    }
    public void getUpdateAllPullRequestData() throws JSONException {
        AuthorService authorService = new AuthorService();
        boolean check = true;
        Date requestOneWeekPrevious = null;
        int start = 0;
        boolean isLastPage = false;
        try{
            while (!isLastPage) {
                HttpResponse<JsonNode> httpResponse = response.getResponse(BitbucketConstants.EndPoints.ALL_PRS_DAILY_ALL_UPDATE + start, BitbucketConstants.Bearer.TOKEN);
                JSONObject body = httpResponse.getBody().getObject();
                Object values = body.get("values");
                JSONArray array = (JSONArray) values;
                for (int i = 0; i < array.length(); i++) {
                    JSONObject object = array.getJSONObject(i);
                    int pullRequestId = (int) object.get("id");
                    int currentVersion = (int) object.get("version");
                    String state = (String) object.get("state");
                    JSONObject author = object.getJSONObject("author");
                    JSONObject user = author.getJSONObject("user");
                    String authorName = (String) user.get("displayName");
                    long updatedDate = (long) object.get("updatedDate");

                    int version = getPullRequestVersion(pullRequestId);
                    requestUpdateDate = new Date(updatedDate);
                    if(check){
                        requestOneWeekPrevious = Date.valueOf(requestUpdateDate.toLocalDate().minusWeeks(1));
                        check = false;
                    }
                       if (checkPullRequest(pullRequestId)) {
                           if (IS_UPDATE_LOGGING) {
                               Log.logger(Log.LogConstant.TAG_INFO,
                                       "Pull Request " + state + " update date " + requestUpdateDate + " ID : " + pullRequestId);
                           }
                           baseService.commonPullRequestDataParser(array.getJSONObject(i));
                           if (checkAuthorName(authorName)) {
                               authorService.insertAuthorData(authorName);
                           } else {
                               authorUpdate(authorName);
                           }
                           IS_UPDATE = true;
                       }
                       if(!(currentVersion == version)){
                           deletePullRequestRelational(pullRequestId);
                           if (IS_UPDATE_LOGGING) {
                               Log.logger(Log.LogConstant.TAG_INFO,
                                       "Pull Request " + state + " update date " + requestUpdateDate + " ID : " + pullRequestId);
                           }
                           baseService.commonPullRequestDataParser(array.getJSONObject(i));
                           if (checkAuthorName(authorName)) {
                               authorService.insertAuthorData(authorName);
                           } else {
                               authorUpdate(authorName);
                           }
                           IS_UPDATE = true;
                       }
                    if (requestOneWeekPrevious.after(requestUpdateDate)) {
                        return;
                    }
                }
                isLastPage = (boolean) body.get("isLastPage");
                start += 10;
            }
        }catch (Exception exception){
            if (IS_UPDATE_LOGGING) {Log.logger(Log.LogConstant.TAG_WARN, String.valueOf(exception));}
        }
    }
    public void deletePullRequestRelational(int pullRequestId) throws SQLException {
        deleteOldPullRequest(pullRequestId);
        deleteOldPullRequestReviewId(pullRequestId);
        deleteOldPullRequestReviewRelation(pullRequestId);
    }
    public void deleteOldPullRequest(int pullRequestId) throws SQLException {
        Connection connection = null;
        PreparedStatement preparedStatementForDelete = null;
        try {
            connection = TransactionManager.getConnection();
            preparedStatementForDelete = connection.prepareStatement(SQL_GET_REVIEW_PULL_REQUEST_DELETE_OLD_PR);
            preparedStatementForDelete.setInt(1, pullRequestId);
            preparedStatementForDelete.executeUpdate();
            connection.commit();
        } catch (Exception exception) {
            if (IS_UPDATE_LOGGING) {
                Log.logger(Log.LogConstant.TAG_WARN, String.valueOf(exception));
            }
        } finally {
            preparedStatementForDelete.close();
            connection.close();
        }
    }
    public void deleteOldPullRequestReviewRelation(int pullRequestId) throws SQLException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = TransactionManager.getConnection();
            preparedStatement = connection.prepareStatement(SQL_GET_PULL_REQUEST_REVIEW_RELATION_DELETE);
            preparedStatement.setInt(1, pullRequestId);
            preparedStatement.executeUpdate();
            connection.commit();
        } catch (Exception exception) {
            if (IS_UPDATE_LOGGING) {
                Log.logger(Log.LogConstant.TAG_WARN, String.valueOf(exception));
            }
        } finally {
            preparedStatement.close();
            connection.close();
        }
    }
    public void deleteOldPullRequestReviewId(int pullRequestId)throws SQLException {
        ArrayList<Integer> reviewId = getPullRequestFindReviewId(pullRequestId);
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = TransactionManager.getConnection();
            preparedStatement = connection.prepareStatement(SQL_GET_PULL_REQUEST_REVIEW_ID_DELETE);
            for (int i = 0; i < reviewId.size(); i++) {
                preparedStatement.setInt(1, reviewId.get(i));
                preparedStatement.executeUpdate();
                connection.commit();
            }
        } catch (Exception exception) {
            if (IS_UPDATE_LOGGING) {Log.logger(Log.LogConstant.TAG_WARN, String.valueOf(exception));}
        } finally {
            preparedStatement.close();
            connection.close();
        }
    }
    public ArrayList<Integer> getPullRequestFindReviewId (int pullRequestId) throws SQLException {
        ArrayList<Integer> reviewIdList = new ArrayList<Integer>();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = TransactionManager.getConnection();
            preparedStatement = connection.prepareStatement(SQL_GET_PULL_REQUEST_FIND_REVIEW_ID);
            preparedStatement.setInt(1, pullRequestId);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                reviewIdList.add(resultSet.getInt(DBConstants.PullRequest.PULL_REQUEST_ID));
            }
        } catch (Exception exception) {
            if (IS_UPDATE_LOGGING) {Log.logger(Log.LogConstant.TAG_WARN, String.valueOf(exception));}
        } finally {
            resultSet.close();
            preparedStatement.close();
            connection.close();
        }
        return reviewIdList;
    }
    public int getPullRequestVersion(int pullRequestId) throws SQLException {
        int version = 0;
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = TransactionManager.getConnection();
            preparedStatement = connection.prepareStatement(SQL_GET_PULL_REQUEST_VERSION);
            preparedStatement.setInt(1, pullRequestId);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                version = resultSet.getInt(DBConstants.PullRequest.PULL_REQUEST_VERSION);
            }
        } catch (Exception exception) {
            if (IS_UPDATE_LOGGING) {
                Log.logger(Log.LogConstant.TAG_WARN, String.valueOf(exception));}
        } finally {
            resultSet.close();
            preparedStatement.close();
            connection.close();
        }
        return version;
    }
    public void authorUpdate(String authorName) throws SQLException{
        AuthorService authorService = new AuthorService();
        authorService.getAuthorUpdateList(authorName);
    }
    public boolean checkAuthorName(String authorName) throws SQLException{
        int existAuthorName = 0;
        Connection connection = null;
        PreparedStatement preparedStmt = null;
        ResultSet resultSet = null;
        try {
            String checkUsername = null;
            connection = TransactionManager.getConnection();
            preparedStmt = connection.prepareStatement(SQL_GET_CHECK_AUTHOR);
            preparedStmt.setString(1, authorName);
            resultSet = preparedStmt.executeQuery();
            connection.commit();
            while (resultSet.next()) {
                existAuthorName = resultSet.getInt(DBConstants.Administrator.CHECK_ADMINISTRATOR_USERNAME);
            }
        }catch (Exception exception){
            if (IS_UPDATE_LOGGING) {
                Log.logger(Log.LogConstant.TAG_WARN, String.valueOf(exception));
            }
        }finally {
            resultSet.close();
            preparedStmt.close();
            connection.close();
        }
        return existAuthorName > 0 ? false : true;
    }
    public boolean checkPullRequest(int PR_id) throws SQLException{
        int existPullRequest = 0;
        Connection connection = null;
        PreparedStatement preparedStmt = null;
        ResultSet resultSet = null;
        try {
            String checkUsername = null;
            connection = TransactionManager.getConnection();
            preparedStmt = connection.prepareStatement(SQL_GET_CHECK_PULL_REQUEST_ID);
            preparedStmt.setInt(1, PR_id);
            resultSet = preparedStmt.executeQuery();
            connection.commit();
            while (resultSet.next()) {
                existPullRequest = resultSet.getInt(DBConstants.Administrator.CHECK_ADMINISTRATOR_USERNAME);
            }
        }catch (Exception exception){
            if (IS_UPDATE_LOGGING) {
                Log.logger(Log.LogConstant.TAG_WARN, String.valueOf(exception));
            }
        }finally {
            resultSet.close();
            preparedStmt.close();
            connection.close();
        }
        return existPullRequest > 0 ? false : true;
    }
    public void truncateReviewerTable()throws SQLException {
        Connection connection = null;
        Statement statement = null;
        try {
            connection = TransactionManager.getConnection();
            statement = connection.createStatement();
            statement.executeUpdate(SQL_TRUNCATE_REVIEWER_TABLE);
            connection.commit();
        } catch (Exception exception) {
            if (IS_UPDATE_LOGGING) {Log.logger(Log.LogConstant.TAG_WARN, String.valueOf(exception));}
        } finally {
            statement.close();
            connection.close();
        }
    }
}