package com.orion.bitbucket.Bitbucket.service;

import java.sql.*;
import java.util.ArrayList;

import com.orion.bitbucket.Bitbucket.dbc.TransactionManager;
import com.orion.bitbucket.Bitbucket.model.PullRequestDO;
import org.springframework.stereotype.Service;

@Service
public class PullRequestService extends BaseService implements PullRequestServiceIF {

    private final String SQL_GET_ALL_PR_COUNT = "select count(*) from pullrequest;";
    private final String SQL_GET_PR_COUNT_BY_STATE = "select count(*) from pullrequest where state=?;";
    private final String SQL_GET_PR_LIST_BY_STATE = "select * from pullrequest where state=?;";
    private final String SQL_GET_PR_LIST_BY_STATE_AND_USERNAME = "select * from pullrequest where state=? and display_name=?;";
    private final String SQL_GET_PULL_REQUEST_BY_ID = "select * from pullrequest where id=?;";

    public int getAllPRCount() throws SQLException {
        Connection connection = TransactionManager.getConnection();
        int count = 0;
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(SQL_GET_ALL_PR_COUNT);
        while (resultSet.next()) {
            count = resultSet.getInt("count");
        }
        resultSet.close();
        statement.close();
        connection.close();
        return count;
    }

    public int getPRCountByState(String status) throws SQLException {
        Connection connection = TransactionManager.getConnection();
        int count = 0;
        PreparedStatement preparedStmt = null;
        preparedStmt = connection.prepareStatement(SQL_GET_PR_COUNT_BY_STATE);
        preparedStmt.setString(1, status);
        ResultSet resultSet = preparedStmt.executeQuery();
        connection.commit();
        while (resultSet.next()) {
            count = resultSet.getInt("count");
        }
        resultSet.close();
        preparedStmt.close();
        connection.close();
        return count;
    }

    public ArrayList<PullRequestDO> getPRListByState(String status) throws SQLException {
        ArrayList<PullRequestDO> list = new ArrayList<PullRequestDO>();
        Connection connection = TransactionManager.getConnection();
        PreparedStatement preparedStmt = null;
        preparedStmt = connection.prepareStatement(SQL_GET_PR_LIST_BY_STATE);
        preparedStmt.setString(1, status);
        ResultSet resultSet = preparedStmt.executeQuery();
        connection.commit();
        while (resultSet.next()) {
            int id = resultSet.getInt("id");
            String title = resultSet.getString("title");
            String state = resultSet.getString("state");
            boolean closed = resultSet.getBoolean("closed");
            String description = resultSet.getString("description");
            String updatedDate = resultSet.getString("update_date"); // long olsun. db ona gore duzeltilsin
            String createdDate = resultSet.getString("created_date"); // long olsun. db ona gore duzeltilsin
            String closedDate = resultSet.getString("closed_date"); // long olsun. db ona gore duzeltilsin
            String emailAddress = resultSet.getString("email_address");
            String displayName = resultSet.getString("display_name");
            String slug = resultSet.getString("slug");
            list.add(new PullRequestDO(id, title, state, closed, description, updatedDate, createdDate, closedDate, emailAddress, displayName, slug, null));
        }
        resultSet.close();
        preparedStmt.close();
        connection.close();
        return list;
    }

    public ArrayList<PullRequestDO> getPRListByStateAndUsername(String status, String username) throws SQLException {
        ArrayList<PullRequestDO> list = new ArrayList<PullRequestDO>();
        Connection connection = TransactionManager.getConnection();
        PreparedStatement preparedStmt = null;
        preparedStmt = connection.prepareStatement(SQL_GET_PR_LIST_BY_STATE_AND_USERNAME);
        preparedStmt.setString(1, status);
        preparedStmt.setString(2, username);
        ResultSet resultSet = preparedStmt.executeQuery();
        connection.commit();
        while (resultSet.next()) {
            int id = resultSet.getInt("id");
            String title = resultSet.getString("title");
            String state = resultSet.getString("state");
            boolean closed = resultSet.getBoolean("closed");
            String description = resultSet.getString("description");
            String updatedDate = resultSet.getString("update_date"); // long olsun. db ona gore duzeltilsin
            String createdDate = resultSet.getString("created_date"); // long olsun. db ona gore duzeltilsin
            String closedDate = resultSet.getString("closed_date"); // long olsun. db ona gore duzeltilsin
            String emailAddress = resultSet.getString("email_address");
            String displayName = resultSet.getString("display_name");
            String slug = resultSet.getString("slug");
            list.add(new PullRequestDO(id, title, state, closed, description, updatedDate, createdDate, closedDate, emailAddress, displayName, slug, null));
        }
        resultSet.close();
        preparedStmt.close();
        connection.close();
        return list;
    }

    public int getPRCountByStateAndUsername(String state, String username) throws SQLException {
        return getPRListByStateAndUsername(state, username).size();
    }

    public PullRequestDO getPullRequestById(int pullRequestId) throws SQLException {
        PullRequestDO pullRequest = null;
        Connection connection = TransactionManager.getConnection();
        PreparedStatement preparedStmt = null;
        preparedStmt = connection.prepareStatement(SQL_GET_PULL_REQUEST_BY_ID);
        preparedStmt.setInt(1, pullRequestId);
        ResultSet resultSet = preparedStmt.executeQuery();
        connection.commit();
        while (resultSet.next()) {
            int id = resultSet.getInt("id");
            String title = resultSet.getString("title");
            String state = resultSet.getString("state");
            boolean closed = resultSet.getBoolean("closed");
            String description = resultSet.getString("description");
            String updatedDate = resultSet.getString("update_date"); // long olsun. db ona gore duzeltilsin
            String createdDate = resultSet.getString("created_date"); // long olsun. db ona gore duzeltilsin
            String closedDate = resultSet.getString("closed_date"); // long olsun. db ona gore duzeltilsin
            String emailAddress = resultSet.getString("email_address");
            String displayName = resultSet.getString("display_name");
            String slug = resultSet.getString("slug");
            pullRequest = new PullRequestDO(id, title, state, closed, description, updatedDate, createdDate, closedDate, emailAddress, displayName, slug, null);
        }
        resultSet.close();
        preparedStmt.close();
        connection.close();
        return pullRequest;
    }

}