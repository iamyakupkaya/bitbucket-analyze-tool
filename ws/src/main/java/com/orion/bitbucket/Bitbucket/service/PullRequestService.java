package com.orion.bitbucket.Bitbucket.service;

import java.sql.*;
import java.util.ArrayList;

import com.orion.bitbucket.Bitbucket.dbc.TransactionManager;
import com.orion.bitbucket.Bitbucket.model.PullRequestDO;
import org.springframework.stereotype.Service;

@Service
public class PullRequestService extends BaseService implements PullRequestServiceIF {

    private final String SQL_GET_ALL_PR_COUNT = "select count(*) from pullrequest;";
    private final String SQL_GET_MERGED_PR_COUNT = "select count(*) from pullrequest where state='MERGED';";
    private final String SQL_GET_OPEN_PR_COUNT = "select count(*) from pullrequest where state='OPEN';";
    private final String SQL_GET_DECLINED_PR_COUNT = "select count(*) from pullrequest where state='DECLINED';";
    private final String SQL_GET_MERGED_PR_LIST = "select * from pullrequest where state='MERGED';";
    private final String SQL_GET_OPEN_PR_LIST = "select * from pullrequest where state='OPEN';";
    private final String SQL_GET_DECLINED_PR_LIST = "select * from pullrequest where state='DECLINED';";
    private final String SQL_GET_MERGED_PR_LIST_BY_USERNAME = "select * from pullrequest where state='MERGED' and display_name=?;";
    private final String SQL_GET_OPEN_PR_LIST_BY_USERNAME = "select * from pullrequest where state='OPEN' and display_name=?;";
    private final String SQL_GET_DECLINED_PR_LIST_BY_USERNAME = "select * from pullrequest where state='DECLINED' and display_name=?;";

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

    public int getMergedPRCount() throws SQLException {
        Connection connection = TransactionManager.getConnection();
        int count = 0;
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(SQL_GET_MERGED_PR_COUNT);
        while (resultSet.next()) {
            count = resultSet.getInt("count");
        }
        resultSet.close();
        statement.close();
        connection.close();
        return count;
    }

    public int getOpenPRCount() throws SQLException {
        Connection connection = TransactionManager.getConnection();
        int count = 0;
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(SQL_GET_OPEN_PR_COUNT);
        while (resultSet.next()) {
            count = resultSet.getInt("count");
        }
        resultSet.close();
        statement.close();
        connection.close();
        return count;
    }

    public int getDeclinedPRCount() throws SQLException {
        Connection connection = TransactionManager.getConnection();
        int count = 0;
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(SQL_GET_DECLINED_PR_COUNT);
        while (resultSet.next()) {
            count = resultSet.getInt("count");
        }
        resultSet.close();
        statement.close();
        connection.close();
        return count;
    }

    public ArrayList<PullRequestDO> getMergedPRList() throws SQLException {
        ArrayList<PullRequestDO> mergedPRList = new ArrayList<PullRequestDO>();
        Connection connection = TransactionManager.getConnection();
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(SQL_GET_MERGED_PR_LIST);
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
            mergedPRList.add(new PullRequestDO(id, title, state, closed, description, updatedDate, createdDate, closedDate, emailAddress, displayName, slug, null));
        }
        resultSet.close();
        statement.close();
        connection.close();
        return mergedPRList;
    }

    public ArrayList<PullRequestDO> getOpenPRList() throws SQLException {
        ArrayList<PullRequestDO> openPRList = new ArrayList<PullRequestDO>();
        Connection connection = TransactionManager.getConnection();
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(SQL_GET_OPEN_PR_LIST);
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
            mergedPRList.add(new PullRequestDO(id, title, state, closed, description, updatedDate, createdDate, closedDate, emailAddress, displayName, slug, null));
        }
        resultSet.close();
        statement.close();
        connection.close();
        return openPRList;
    }

    public ArrayList<PullRequestDO> getDeclinedPRList() throws SQLException {
        ArrayList<PullRequestDO> declinedPRList = new ArrayList<PullRequestDO>();
        Connection connection = TransactionManager.getConnection();
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(SQL_GET_DECLINED_PR_LIST);
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
            mergedPRList.add(new PullRequestDO(id, title, state, closed, description, updatedDate, createdDate, closedDate, emailAddress, displayName, slug, null));
        }
        resultSet.close();
        statement.close();
        connection.close();
        return declinedPRList;
    }

    public ArrayList<PullRequestDO> getMergedPRListByUsername(String username) throws SQLException {
        ArrayList<PullRequestDO> list = new ArrayList<PullRequestDO>();
        Connection connection = TransactionManager.getConnection();
        PreparedStatement preparedStmt = null;
        preparedStmt = connection.prepareStatement(SQL_GET_MERGED_PR_LIST_BY_USERNAME);
        preparedStmt.setString(1, username);
        ResultSet resultSet = preparedStmt.executeQuery();
        connection.commit(); // ADDED
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

    public ArrayList<PullRequestDO> getOpenPRListByUsername(String username) throws SQLException {
        ArrayList<PullRequestDO> list = new ArrayList<PullRequestDO>();
        Connection connection = TransactionManager.getConnection();
        PreparedStatement preparedStmt = null;
        preparedStmt = connection.prepareStatement(SQL_GET_OPEN_PR_LIST_BY_USERNAME);
        preparedStmt.setString(1, username);
        ResultSet resultSet = preparedStmt.executeQuery();
        connection.commit(); // ADDED
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

    public ArrayList<PullRequestDO> getDeclinedPRListByUsername(String username) throws SQLException {
        ArrayList<PullRequestDO> list = new ArrayList<PullRequestDO>();
        Connection connection = TransactionManager.getConnection();
        PreparedStatement preparedStmt = null;
        preparedStmt = connection.prepareStatement(SQL_GET_DECLINED_PR_LIST_BY_USERNAME);
        preparedStmt.setString(1, username);
        ResultSet resultSet = preparedStmt.executeQuery();
        connection.commit(); // ADDED
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

    public int getMergedPRCountByUsername(String username) throws SQLException {
        return getMergedPRListByUsername(username).size();
    }

    public int getOpenPRCountByUsername(String username) throws SQLException {
        return getOpenPRListByUsername(username).size();
    }

    public int getDeclinedPRCountByUsername(String username) throws SQLException {
        return getDeclinedPRListByUsername(username).size();
    }

}