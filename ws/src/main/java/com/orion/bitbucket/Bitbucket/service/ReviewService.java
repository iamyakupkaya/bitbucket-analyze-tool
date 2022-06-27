package com.orion.bitbucket.Bitbucket.service;

import java.sql.*;
import java.util.ArrayList;
import com.orion.bitbucket.Bitbucket.dbc.DBConstants;
import com.orion.bitbucket.Bitbucket.dbc.TransactionManager;
import com.orion.bitbucket.Bitbucket.model.PullRequestDO;
import com.orion.bitbucket.Bitbucket.model.ReviewDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.orion.bitbucket.Bitbucket.log.Log;
@Service
public class ReviewService extends BaseService implements ReviewServiceIF {

    @Autowired
    private PullRequestServiceIF pullRequestServiceIF;

    private boolean IS_REVIEW_LOGGING = false;
    private final String SQL_GET_ALL_REVIEW_COUNT = "select count(*) from review;";
    private final String SQL_GET_REVIEWS_BY_USERNAME_AND_STATUS = "select * from review where display_name=? and status=?;";
    private final String SQL_GET_REVIEWS_BY_USERNAME = "select * from review where display_name=?;";
    private final String SQL_GET_REVIEWS_BY_ID = "select * from review where id=?;";
    private final String SQL_GET_REVIEWS_BY_ID_AND_STATUS = "select * from review where id=? and status=?;";
    private final String SQL_GET_PULL_REQUEST_ID_FROM_RELATION_TABLE = "select pull_request_id from pullrequestreviewrelation where review_id=?;";
    private final String SQL_GET_REVIEW_ID_FROM_RELATION_TABLE = "select review_id from pullrequestreviewrelation where pull_request_id=?;";
    private final String SQL_GET_REVIEW_ID_BY_USERNAME = "select id from review where display_name=? order by id desc limit 1;";
    private final String SQL_GET_MOST_OF_REVIEWED_PULL_REQUEST = " select pull_request_id,count(*) as mostOf from pullrequestreviewrelation group by pull_request_id having count(*) =(select max(mostOf) from  (select pull_request_id,count(*) as mostOf from pullrequestreviewrelation group by pull_request_id) pullrequestreviewrelation)";
    private final String SQL_GET_REWIEW_ID_FROM_RELATION_TABLE = "select review_id from pullrequestreviewrelation where pull_request_id=?;";
    

    public int getTotalReviewCount() throws SQLException {
        Connection connection = TransactionManager.getConnection();
        int count = 0;
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(SQL_GET_ALL_REVIEW_COUNT);
        while (resultSet.next()) {
            count = resultSet.getInt("count");
        }
        resultSet.close();
        statement.close();
        connection.close();
        return count;
    }

    public ArrayList<ReviewDO.PullRequestReviewRelation> getReviewsByUsernameAndStatus(String username, String status) throws SQLException {
        ArrayList<ReviewDO> list = new ArrayList<>();
        Connection connection = TransactionManager.getConnection();
        PreparedStatement preparedStmt = null;
        preparedStmt = connection.prepareStatement(SQL_GET_REVIEWS_BY_USERNAME_AND_STATUS);
        preparedStmt.setString(1, username);
        preparedStmt.setString(2, status);
        ResultSet resultSet = preparedStmt.executeQuery();
        connection.commit();
        while (resultSet.next()) {
            int id = resultSet.getInt(DBConstants.Review.REVIEW_ID);
            String displayName = resultSet.getString(DBConstants.Review.REVIEW_DISPLAY_NAME);
            String emailAddress = resultSet.getString(DBConstants.Review.REVIEW_EMAIL_ADDRESS);
            boolean approved = resultSet.getBoolean(DBConstants.Review.REVIEW_APPROVED);
            String ReviewStatus = resultSet.getString(DBConstants.Review.REVIEW_STATUS);
            list.add(new ReviewDO(id, displayName, emailAddress, ReviewStatus, approved));
        }
        resultSet.close();
        preparedStmt.close();
        connection.close();

        ArrayList<ReviewDO.PullRequestReviewRelation> pullRequestReviewRelations = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            connection = TransactionManager.getConnection();
            int pullRequestId = 0;
            preparedStmt = null;
            preparedStmt = connection.prepareStatement(SQL_GET_PULL_REQUEST_ID_FROM_RELATION_TABLE);
            preparedStmt.setInt(1, list.get(i).getId());
            resultSet = preparedStmt.executeQuery();
            connection.commit();
            while (resultSet.next()) {
                pullRequestId = resultSet.getInt(DBConstants.PullRequestReviewRelation.REVIEW_RELATION_PULL_REQUEST_ID);
            }
            resultSet.close();
            preparedStmt.close();
            connection.close();

            PullRequestDO pullRequest = pullRequestServiceIF.getPullRequestById(pullRequestId);
            pullRequestReviewRelations.add(new ReviewDO.PullRequestReviewRelation(pullRequest, list.get(i)));
            
        }
        return pullRequestReviewRelations;
    }

    public ArrayList<ReviewDO.PullRequestReviewRelation> getReviewsByUsername(String username) throws SQLException {
        ArrayList<ReviewDO> list = new ArrayList<>();
        Connection connection = TransactionManager.getConnection();
        PreparedStatement preparedStmt = null;
        preparedStmt = connection.prepareStatement(SQL_GET_REVIEWS_BY_USERNAME);
        preparedStmt.setString(1, username);
        ResultSet resultSet = preparedStmt.executeQuery();
        connection.commit();
        while (resultSet.next()) {
            int id = resultSet.getInt(DBConstants.Review.REVIEW_ID);
            String displayName = resultSet.getString(DBConstants.Review.REVIEW_DISPLAY_NAME);
            String emailAddress = resultSet.getString(DBConstants.Review.REVIEW_EMAIL_ADDRESS);
            boolean approved = resultSet.getBoolean(DBConstants.Review.REVIEW_APPROVED);
            String ReviewStatus = resultSet.getString(DBConstants.Review.REVIEW_STATUS);
            list.add(new ReviewDO(id, displayName, emailAddress, ReviewStatus, approved));
        }
        resultSet.close();
        preparedStmt.close();
        connection.close();

        ArrayList<ReviewDO.PullRequestReviewRelation> pullRequestReviewRelations = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            connection = TransactionManager.getConnection();
            int pullRequestId = 0;
            preparedStmt = null;
            preparedStmt = connection.prepareStatement(SQL_GET_PULL_REQUEST_ID_FROM_RELATION_TABLE);
            preparedStmt.setInt(1, list.get(i).getId());
            resultSet = preparedStmt.executeQuery();
            connection.commit();
            while (resultSet.next()) {
                pullRequestId = resultSet.getInt(DBConstants.PullRequestReviewRelation.REVIEW_RELATION_PULL_REQUEST_ID);
            }
            resultSet.close();
            preparedStmt.close();
            connection.close();

            PullRequestDO pullRequest = pullRequestServiceIF.getPullRequestById(pullRequestId);
            pullRequestReviewRelations.add(new ReviewDO.PullRequestReviewRelation(pullRequest, list.get(i)));
            
        }
        return pullRequestReviewRelations;
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
            id = resultSet.getInt(DBConstants.Review.REVIEW_ID);
        }
        resultSet.close();
        preparedStmt.close();
        connection.close();
        return id;
    }

    public ArrayList<ReviewDO.PullRequestReviewRelation> getReviewsWithPullRequestState(String state)
            throws SQLException {
        ArrayList<PullRequestDO> pullRequestList = pullRequestServiceIF.getPRListByState(state);
        ArrayList<ReviewDO.PullRequestReviewRelation> pullRequestReviewRelations = new ArrayList<>();

        for (int i = 0; i < pullRequestList.size(); i++) {
            PullRequestDO pullRequest = pullRequestServiceIF.getPullRequestById(pullRequestList.get(i).getPrId());
            Connection connection = TransactionManager.getConnection();
            PreparedStatement preparedStmt = null;
            preparedStmt = connection.prepareStatement(SQL_GET_REWIEW_ID_FROM_RELATION_TABLE);
            preparedStmt.setInt(1, pullRequestList.get(i).getPrId());
            ResultSet resultSet = preparedStmt.executeQuery();
            connection.commit();
            while (resultSet.next()) {
                int review_id = resultSet.getInt(DBConstants.PullRequestReviewRelation.REVIEW_RELATION_REVIEW_ID);
                PreparedStatement preparedStmtTwo = null;
                preparedStmtTwo = connection.prepareStatement(SQL_GET_REVIEWS_BY_ID);
                preparedStmtTwo.setInt(1, review_id);
                ResultSet resultSetTwo = preparedStmtTwo.executeQuery();
                connection.commit();
                while (resultSetTwo.next()) {
                    int id = resultSetTwo.getInt(DBConstants.Review.REVIEW_ID);
                    String displayName = resultSetTwo.getString(DBConstants.Review.REVIEW_DISPLAY_NAME);
                    String emailAddress = resultSetTwo.getString(DBConstants.Review.REVIEW_EMAIL_ADDRESS);
                    boolean approved = resultSetTwo.getBoolean(DBConstants.Review.REVIEW_APPROVED);
                    String ReviewStatus = resultSetTwo.getString(DBConstants.Review.REVIEW_STATUS);
                    pullRequestReviewRelations.add(new ReviewDO.PullRequestReviewRelation(pullRequest,
                            new ReviewDO(id, displayName, emailAddress, ReviewStatus, approved)));
                }
                resultSetTwo.close();
                preparedStmtTwo.close();
            }
            resultSet.close();
            preparedStmt.close();
            connection.close();
        }

        return pullRequestReviewRelations;
    }


    public ArrayList<ReviewDO.PullRequestReviewRelation> mostReviewedPullRequest() throws SQLException {
        ArrayList<ReviewDO.PullRequestReviewRelation> pullRequestReviewRelations = new ArrayList<>();
        Connection connection = TransactionManager.getConnection();
        int pull_request_id = 0;
        int reviewCount = 0;
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(SQL_GET_MOST_OF_REVIEWED_PULL_REQUEST);
        while (resultSet.next()) {
            pull_request_id = resultSet.getInt(DBConstants.PullRequestReviewRelation.REVIEW_RELATION_PULL_REQUEST_ID);
            reviewCount = resultSet.getInt("mostOf");
        }
        resultSet.close();
        statement.close();
        connection.close();
        PullRequestDO pullRequest = pullRequestServiceIF.getPullRequestById(pull_request_id);
        connection = TransactionManager.getConnection();
        PreparedStatement preparedStmt = null;
        preparedStmt = connection.prepareStatement(SQL_GET_REWIEW_ID_FROM_RELATION_TABLE);
        preparedStmt.setInt(1, pull_request_id);
        resultSet = preparedStmt.executeQuery();
        connection.commit();
        while (resultSet.next()) {
            int review_id = resultSet.getInt(DBConstants.PullRequestReviewRelation.REVIEW_RELATION_REVIEW_ID);
            PreparedStatement preparedStmtTwo = null;
            preparedStmtTwo = connection.prepareStatement(SQL_GET_REVIEWS_BY_ID);
            preparedStmtTwo.setInt(1, review_id);
            ResultSet resultSetTwo = preparedStmtTwo.executeQuery();
            connection.commit();
            while (resultSetTwo.next()) {
                    int id = resultSetTwo.getInt(DBConstants.Review.REVIEW_ID);
                    String displayName = resultSetTwo.getString(DBConstants.Review.REVIEW_DISPLAY_NAME);
                    String emailAddress = resultSetTwo.getString(DBConstants.Review.REVIEW_EMAIL_ADDRESS);
                    boolean approved = resultSetTwo.getBoolean(DBConstants.Review.REVIEW_APPROVED);
                    String ReviewStatus = resultSetTwo.getString(DBConstants.Review.REVIEW_STATUS);
                pullRequestReviewRelations.add(new ReviewDO.PullRequestReviewRelation(pullRequest,
                        new ReviewDO(id, displayName, emailAddress, ReviewStatus, approved)));
            }
            resultSetTwo.close();
            preparedStmtTwo.close();
        }
        resultSet.close();
        preparedStmt.close();
        connection.close();
        
        return pullRequestReviewRelations;

    }

    public ArrayList<ReviewDO> getReviewsWithPullRequestIdAndStatus(int id, String status) throws SQLException {
        ArrayList<Integer> reviewIdList = new ArrayList<>();
        Connection connection = TransactionManager.getConnection();
        PreparedStatement preparedStmt = null;
        preparedStmt = connection.prepareStatement(SQL_GET_REVIEW_ID_FROM_RELATION_TABLE);
        preparedStmt.setInt(1, id);
        ResultSet resultSet = preparedStmt.executeQuery();
        connection.commit();
        while (resultSet.next()) {
            reviewIdList.add(resultSet.getInt(DBConstants.PullRequestReviewRelation.REVIEW_RELATION_REVIEW_ID));
        }
        resultSet.close();
        preparedStmt.close();
        connection.close();
        
        ArrayList<ReviewDO> reviewerList = new ArrayList<>();
        for (int i = 0; i < reviewIdList.size(); i++) {
        connection = TransactionManager.getConnection();
        preparedStmt = connection.prepareStatement(SQL_GET_REVIEWS_BY_ID_AND_STATUS);
        preparedStmt.setInt(1, reviewIdList.get(i));
        preparedStmt.setString(2,status);
        resultSet = preparedStmt.executeQuery();
        connection.commit();
        while (resultSet.next()) {
            int reviewerId = resultSet.getInt(DBConstants.Review.REVIEW_ID);
            String displayName = resultSet.getString(DBConstants.Review.REVIEW_DISPLAY_NAME);
            String emailAddress = resultSet.getString(DBConstants.Review.REVIEW_EMAIL_ADDRESS);
            boolean approved = resultSet.getBoolean(DBConstants.Review.REVIEW_APPROVED);
            String ReviewStatus = resultSet.getString(DBConstants.Review.REVIEW_STATUS);
                reviewerList.add(new ReviewDO(reviewerId, displayName, emailAddress, ReviewStatus, approved));
        }
        resultSet.close();
        preparedStmt.close();
        connection.close();
        }

        return reviewerList;
    }

}
