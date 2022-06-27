package com.orion.bitbucket.Bitbucket.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import com.orion.bitbucket.Bitbucket.dbc.DBConstants;
import com.orion.bitbucket.Bitbucket.dbc.TransactionManager;
import com.orion.bitbucket.Bitbucket.model.ReviewerDO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.orion.bitbucket.Bitbucket.log.Log;

@Service
public class ReviewerService extends BaseService implements ReviewerServiceIF {

    private static int COUNTER = 1000;
    private boolean IS_REVIEWER_LOGGING = false;

    @Autowired
    private ReviewServiceIF reviewServiceIF;

    private final String SQL_GET_REVIEWER_COUNT = "select count(name) from reviewer";
    private final String SQL_INSERT_REVIEWER_DATA = "insert into reviewer(id, name, total_review, total_approve, total_unapprove) values (?, ?, ?, ?, ?);";
    private final String GET_SQL_ALL_REVIEW_DISPLAY_NAME = "SELECT display_name FROM review GROUP BY display_name";
    private final String SQL_GET_COUNT_REVIEW_BY_STATUS_AND_USERNAME = "select count(*) from review where status=? and display_name=?;";
    private final String SQL_IS_REVIEWER_TABLE_EMPTY = "select count(*) from reviewer;";
    private final String SQL_GET_TOP_REVIEWER = "select name, total_approve from reviewer order by total_approve desc limit 1";
    private final String SQL_GET_ALL_REVIEWER = "select * from reviewer";
    private final String SQL_GET_REVIEWER_BY_USERNAME = "select * from reviewer where name=?;";

    public ArrayList<String> getAllReviewer() throws SQLException {
        ArrayList<String> reviewers = new ArrayList<>();
        Connection connection = TransactionManager.getConnection();
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(GET_SQL_ALL_REVIEW_DISPLAY_NAME);
        while (resultSet.next()) {
            reviewers.add(resultSet.getString(DBConstants.Review.REVIEW_DISPLAY_NAME));
        }
        resultSet.close();
        statement.close();
        connection.close();
        if (isReviewerTableEmpty()) {
            for (int i = 0; i < reviewers.size(); i++) {
                insertReviewerData(reviewers.get(i));
            }
        }
        return reviewers;
    }

    public boolean isReviewerTableEmpty() {
        Connection connection = TransactionManager.getConnection();
        int count = 0;
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(SQL_IS_REVIEWER_TABLE_EMPTY);
            while (resultSet.next()) {
                count = resultSet.getInt(DBConstants.Reviewer.REVIEWER_COUNT);
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

    public ArrayList<ReviewerDO> getAllReviewers() throws SQLException {
        ArrayList<ReviewerDO> reviewers = new ArrayList<ReviewerDO>();
        Connection connection = TransactionManager.getConnection();
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(SQL_GET_ALL_REVIEWER);
        while (resultSet.next()) {
            int id = resultSet.getInt(DBConstants.Reviewer.REVIEWER_ID);
            String reviewerName = resultSet.getString(DBConstants.Reviewer.REVIEWER_NAME);
            int totalReview = resultSet.getInt(DBConstants.Reviewer.REVIEWER_TOTAL_REVIEW);
            int totalApprove = resultSet.getInt(DBConstants.Reviewer.REVIEWER_TOTAL_APPROVE);
            int totalUnapprove = resultSet.getInt(DBConstants.Reviewer.REVIEWER_TOTAL_UNAPPROVE);
            reviewers.add(new ReviewerDO(id, reviewerName, totalReview, totalApprove, totalUnapprove));
        }
        resultSet.close();
        statement.close();
        connection.close();
        return reviewers;
    }

    public int getAllReviewerCount() throws SQLException {
        Connection connection = TransactionManager.getConnection();
        int count = 0;
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(SQL_GET_REVIEWER_COUNT);
        while (resultSet.next()) {
            count = resultSet.getInt(DBConstants.Reviewer.REVIEWER_COUNT);
        }
        resultSet.close();
        statement.close();
        connection.close();
        return count;
    }

    public int getCountReviewByStatusAndUsername(String state, String username) throws SQLException {
        Connection connection = TransactionManager.getConnection();
        int count = 0;
        PreparedStatement preparedStmt = null;
        preparedStmt = connection.prepareStatement(SQL_GET_COUNT_REVIEW_BY_STATUS_AND_USERNAME);
        preparedStmt.setString(1, state);
        preparedStmt.setString(2, username);
        ResultSet resultSet = preparedStmt.executeQuery();
        connection.commit(); 
        while (resultSet.next()) {
            count = resultSet.getInt(DBConstants.Reviewer.REVIEWER_COUNT);
        }
        resultSet.close();
        preparedStmt.close();
        connection.close();
        
        return count;

    }

    
    public void insertReviewerData(String reviewer) throws SQLException {
        int totalReview = reviewServiceIF.getReviewsByUsername(reviewer).size();
        int totalApproved = getCountReviewByStatusAndUsername(DBConstants.Review.REVIEW_STATUS_APPROVED, reviewer);
        int totalUnApproved = getCountReviewByStatusAndUsername(DBConstants.Review.REVIEW_STATUS_UNAPPROVED, reviewer);

        Connection connection = TransactionManager.getConnection();
        try {
            PreparedStatement preparedStmt = null;
            preparedStmt = connection.prepareStatement(SQL_INSERT_REVIEWER_DATA);
            preparedStmt.setInt(1, COUNTER);
            preparedStmt.setString(2, reviewer);
            preparedStmt.setInt(3, totalReview);
            preparedStmt.setInt(4, totalApproved);
            preparedStmt.setInt(5, totalUnApproved);
            int row = preparedStmt.executeUpdate();
            connection.commit();
            COUNTER++;
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        } finally {
            connection.close();
        }
    }

    public ReviewerDO.TopReviewer getTopReviewer() throws SQLException {
        Connection connection = TransactionManager.getConnection();
        ReviewerDO.TopReviewer topReviewer = null;
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(SQL_GET_TOP_REVIEWER);
        while (resultSet.next()) {
            String name = resultSet.getString(DBConstants.Reviewer.REVIEWER_NAME);
            int totalApprovedCount = resultSet.getInt(DBConstants.Reviewer.REVIEWER_TOTAL_APPROVE);
            topReviewer = new ReviewerDO.TopReviewer(name, totalApprovedCount);
        }
        resultSet.close();
        statement.close();
        connection.close();
        return topReviewer;
    }

   
    public ArrayList<ReviewerDO> getCountReviewStatesByUsername(String name) throws SQLException {
        ArrayList<ReviewerDO> list = new ArrayList<>();
        Connection connection = TransactionManager.getConnection();
        PreparedStatement preparedStmt = null;
        preparedStmt = connection.prepareStatement(SQL_GET_REVIEWER_BY_USERNAME);
        preparedStmt.setString(1, name);
        ResultSet resultSet = preparedStmt.executeQuery();
        connection.commit();
        while (resultSet.next()) {
            int id = resultSet.getInt(DBConstants.Reviewer.REVIEWER_ID);
            String reviewerName = resultSet.getString(DBConstants.Reviewer.REVIEWER_NAME);
            int totalReview = resultSet.getInt(DBConstants.Reviewer.REVIEWER_TOTAL_REVIEW);
            int totalApprove = resultSet.getInt(DBConstants.Reviewer.REVIEWER_TOTAL_APPROVE);
            int totalUnapprove = resultSet.getInt(DBConstants.Reviewer.REVIEWER_TOTAL_UNAPPROVE);

            list.add(new ReviewerDO(id, reviewerName, totalReview, totalApprove, totalUnapprove));
        }
        resultSet.close();
        preparedStmt.close();
        connection.close();
        return list;
    }

}
