package com.orion.bitbucket.Bitbucket.service;

import java.sql.*;
import java.util.ArrayList;

import com.orion.bitbucket.Bitbucket.dbc.TransactionManager;
import com.orion.bitbucket.Bitbucket.model.AuthorDO;
import org.springframework.stereotype.Service;

@Service
public class AuthorService extends BaseService implements AuthorServiceIF {

    private static int COUNTER = 1000;

    private final String SQL_GET_AUTHOR_COUNT = "select count(name) from author";
    private final String SQL_GET_ALL_AUTHOR = "select distinct display_name from pullrequest";
    private final String SQL_IS_AUTHOR_TABLE_EMPTY = "select count(*) from author;";
    private final String SQL_GET_COUNT_PR_BY_STATE_AND_USERNAME = "select count(*) from pullrequest where state=? and display_name=?;";
    private final String SQL_INSERT_AUTHOR_DATA = "insert into author(id, name, total_prs, total_merged_prs, total_open_prs, total_declined_prs) values (?, ?, ?, ?, ?, ?);";
    private final String SQL_GET_TOP_AUTHOR = "select name, total_prs from author order by total_prs desc limit 1";
    private final String SQL_GET_TOP_AUTHOR_BY_OPEN = "select name, total_open_prs from author order by total_open_prs desc limit 1";
    private final String SQL_GET_TOP_AUTHOR_BY_MERGED = "select name, total_merged_prs from author order by total_merged_prs desc limit 1";
    private final String SQL_GET_TOP_AUTHOR_BY_DECLINED = "select name, total_declined_prs from author order by total_declined_prs desc limit 1";
    private final String SQL_GET_ALL_AUTHORS = "select * from author";
    private final String SQL_GET_AUTHOR_BY_USERNAME = "select * from author where name=?;";

    public int getAuthorCount() throws SQLException {
        Connection connection = TransactionManager.getConnection();
        int count = 0;
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(SQL_GET_AUTHOR_COUNT);
        while (resultSet.next()) {
            count = resultSet.getInt("count");
        }
        resultSet.close();
        statement.close();
        connection.close();
        return count;
    }

    public ArrayList<String> getAllAuthor() throws SQLException {
        ArrayList<String> authors = new ArrayList<String>();
        Connection connection = TransactionManager.getConnection();
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(SQL_GET_ALL_AUTHOR);
        while (resultSet.next()) {
            String displayName = resultSet.getString("display_name");
            authors.add(displayName);
        }
        resultSet.close();
        statement.close();
        connection.close();
        if (isAuthorTableEmpty()) {
            for (int i = 0; i < authors.size(); i++) {
                insertAuthorData(authors.get(i));
            }
        }
        return authors;
    }

    public boolean isAuthorTableEmpty() {
        Connection connection = TransactionManager.getConnection();
        int count = 0;
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(SQL_IS_AUTHOR_TABLE_EMPTY);
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

    public int getCountPRByStateAndUsername(String state, String username) throws SQLException {
        Connection connection = TransactionManager.getConnection();
        int count = 0;
        PreparedStatement preparedStmt = null;
        preparedStmt = connection.prepareStatement(SQL_GET_COUNT_PR_BY_STATE_AND_USERNAME);
        preparedStmt.setString(1, state);
        preparedStmt.setString(2, username);
        ResultSet resultSet = preparedStmt.executeQuery();
        connection.commit(); // ADDED
        while (resultSet.next()) {
            count = resultSet.getInt("count");
        }
        resultSet.close();
        preparedStmt.close();
        connection.close();
        return count;
    }

    public ArrayList<AuthorDO> getCountOfPrStatesWithDisplayName(String name) throws SQLException {
        ArrayList<AuthorDO> list = new ArrayList<>();
        Connection connection = TransactionManager.getConnection();
        PreparedStatement preparedStmt = null;
        preparedStmt = connection.prepareStatement(SQL_GET_AUTHOR_BY_USERNAME);
        preparedStmt.setString(1, name);
        ResultSet resultSet = preparedStmt.executeQuery();
        connection.commit();
        while (resultSet.next()) {
            int id = resultSet.getInt("id");
            String authorName = resultSet.getString("name");
            int totalPrs = resultSet.getInt("total_prs");
            int totalMerged = resultSet.getInt("total_merged_prs");
            int totalOpen = resultSet.getInt("total_open_prs");
            int totalDeclined = resultSet.getInt("total_declined_prs");

            list.add(new AuthorDO(id, authorName, totalPrs, totalMerged, totalOpen, totalDeclined));
        }
        resultSet.close();
        preparedStmt.close();
        connection.close();

        return list;
    }



    public void insertAuthorData(String author) throws SQLException {
        int totalMergedPRs = getCountPRByStateAndUsername("MERGED", author);
        int totalOpenPRs = getCountPRByStateAndUsername("OPEN", author);
        int totalDeclinedPRs = getCountPRByStateAndUsername("DECLINED", author);
        int totalPRs = totalMergedPRs + totalOpenPRs + totalDeclinedPRs;
        Connection connection = TransactionManager.getConnection();
        try {
            PreparedStatement preparedStmt = null;
            preparedStmt = connection.prepareStatement(SQL_INSERT_AUTHOR_DATA);
            preparedStmt.setInt(1, COUNTER);
            preparedStmt.setString(2, author);
            preparedStmt.setInt(3, totalPRs);
            preparedStmt.setInt(4, totalMergedPRs);
            preparedStmt.setInt(5, totalOpenPRs);
            preparedStmt.setInt(6, totalDeclinedPRs);
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

    public AuthorDO.TopAuthor getTopAuthor() throws SQLException {
        Connection connection = TransactionManager.getConnection();
        AuthorDO.TopAuthor topAuthor = null;
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(SQL_GET_TOP_AUTHOR);
        while (resultSet.next()) {
            String name = resultSet.getString("name");
            int totalPRCount = resultSet.getInt("total_prs");
            topAuthor = new AuthorDO.TopAuthor(name, totalPRCount);
        }
        resultSet.close();
        statement.close();
        connection.close();
        return topAuthor;
    }

    public AuthorDO.TopAuthor getTopAuthorAtOpen() throws SQLException {
        Connection connection = TransactionManager.getConnection();
        AuthorDO.TopAuthor topAuthor = null;
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(SQL_GET_TOP_AUTHOR_BY_OPEN);
        while (resultSet.next()) {
            String name = resultSet.getString("name");
            int totalPRCount = resultSet.getInt("total_open_prs");
            topAuthor = new AuthorDO.TopAuthor(name, totalPRCount);
        }
        resultSet.close();
        statement.close();
        connection.close();
        return topAuthor;
    }

    public AuthorDO.TopAuthor getTopAuthorAtMerged() throws SQLException {
        Connection connection = TransactionManager.getConnection();
        AuthorDO.TopAuthor topAuthor = null;
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(SQL_GET_TOP_AUTHOR_BY_MERGED);
        while (resultSet.next()) {
            String name = resultSet.getString("name");
            int totalPRCount = resultSet.getInt("total_merged_prs");
            topAuthor = new AuthorDO.TopAuthor(name, totalPRCount);
        }
        resultSet.close();
        statement.close();
        connection.close();
        return topAuthor;
    }

    public AuthorDO.TopAuthor getTopAuthorAtDeclined() throws SQLException {
        Connection connection = TransactionManager.getConnection();
        AuthorDO.TopAuthor topAuthor = null;
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(SQL_GET_TOP_AUTHOR_BY_DECLINED);
        while (resultSet.next()) {
            String name = resultSet.getString("name");
            int totalPRCount = resultSet.getInt("total_declined_prs");
            topAuthor = new AuthorDO.TopAuthor(name, totalPRCount);
        }
        resultSet.close();
        statement.close();
        connection.close();
        return topAuthor;
    }

    public ArrayList<AuthorDO> getAllAuthors() throws SQLException {
        ArrayList<AuthorDO> authors = new ArrayList<AuthorDO>();
        Connection connection = TransactionManager.getConnection();
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(SQL_GET_ALL_AUTHORS);
        while (resultSet.next()) {
            int id = resultSet.getInt("id");
            String name = resultSet.getString("name");
            int total = resultSet.getInt("total_prs");
            int merge = resultSet.getInt("total_merged_prs");
            int open = resultSet.getInt("total_open_prs");
            int declined = resultSet.getInt("total_declined_prs");
            authors.add(new AuthorDO(id, name, total, merge, open, declined));
        }
        resultSet.close();
        statement.close();
        connection.close();
        return authors;
    }

}