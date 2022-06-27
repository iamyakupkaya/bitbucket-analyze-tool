package com.orion.bitbucket.Bitbucket.service;

import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Locale;
import com.orion.bitbucket.Bitbucket.dbc.DBConstants;
import com.orion.bitbucket.Bitbucket.dbc.TransactionManager;
import com.orion.bitbucket.Bitbucket.model.AuthorDO;
import org.springframework.stereotype.Service;
import com.orion.bitbucket.Bitbucket.log.Log;

@Service
public class AuthorService extends BaseService implements AuthorServiceIF {

    private static int COUNTER = 1000;
    private boolean IS_AUTHOR_LOGGING = false;

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
    private final String SQL_GET_TOP_AUTHOR_PR_LIST_BY_DATE_INTERVAL = "SELECT display_name, COUNT(*) AS totalPRCount FROM pullrequest WHERE DATE(created_date) >= DATE(NOW()) - ?::INTERVAL  GROUP BY display_name ORDER BY totalPRCount DESC LIMIT 1;";
    private final String SQL_GET_TOP_AUTHOR_PR_LIST_BY_DATE_INTERVAL_AND_PR_STATE = "SELECT display_name, COUNT(*) AS totalPRCount FROM pullrequest WHERE state=? AND DATE(created_date) >= DATE(NOW()) - ?::INTERVAL  GROUP BY display_name ORDER BY totalPRCount DESC LIMIT 1;";
    private final String SQL_GET_ALL_AUTHORS_UPDATE_WITH_FILTER_MERGED_PR = "select count(state) as count from ( select name,created_date,state from author inner join pullrequest on name = display_name where state='MERGED' and closed_date between ? and ? order by total_prs desc) as count where name=?";
    private final String SQL_GET_ALL_AUTHORS_UPDATE_WITH_FILTER_DECLINED_PR = "select count(state) as count from ( select name,created_date,state from author inner join pullrequest on name = display_name where state='DECLINED' and closed_date between ? and ? order by total_prs desc) as count where name=?";
    private final String SQL_GET_ALL_AUTHORS_UPDATE_WITH_FILTER_OPEN_PR = "select count(state) as count from ( select name,created_date,state from author inner join pullrequest on name = display_name where state='OPEN' and created_date between ? and ? order by total_prs desc) as count where name=?";
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
            String displayName = resultSet.getString(DBConstants.PullRequest.PULL_REQUEST_AUTHOR_DISPLAY_NAME);
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

    public ArrayList<AuthorDO> getCountPRStatesByUsername(String name) throws SQLException {
        ArrayList<AuthorDO> list = new ArrayList<>();
        Connection connection = TransactionManager.getConnection();
        PreparedStatement preparedStmt = null;
        preparedStmt = connection.prepareStatement(SQL_GET_AUTHOR_BY_USERNAME);
        preparedStmt.setString(1, name);
        ResultSet resultSet = preparedStmt.executeQuery();
        connection.commit();
        while (resultSet.next()) {
            int id = resultSet.getInt(DBConstants.Author.AUTHOR_ID);
            String authorName = resultSet.getString(DBConstants.Author.AUTHOR_NAME);
            int totalPrs = resultSet.getInt(DBConstants.Author.AUTHOR_TOTAL_PRS);
            int totalMerged = resultSet.getInt(DBConstants.Author.AUTHOR_TOTAL_MERGED_PRS);
            int totalOpen = resultSet.getInt(DBConstants.Author.AUTHOR_TOTAL_OPEN_PRS);
            int totalDeclined = resultSet.getInt(DBConstants.Author.AUTHOR_TOTAL_DECLINED_PRS);
            list.add(new AuthorDO(id, authorName, totalPrs, totalMerged, totalOpen, totalDeclined));
        }
        resultSet.close();
        preparedStmt.close();
        connection.close();

        return list;
    }



    public void insertAuthorData(String author) throws SQLException {
        int totalMergedPRs = getCountPRByStateAndUsername(DBConstants.PullRequestState.MERGED, author);
        int totalOpenPRs = getCountPRByStateAndUsername(DBConstants.PullRequestState.OPEN, author);
        int totalDeclinedPRs = getCountPRByStateAndUsername(DBConstants.PullRequestState.DECLINED, author);
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
            String name = resultSet.getString(DBConstants.Author.AUTHOR_NAME);
            int totalPRCount = resultSet.getInt(DBConstants.Author.AUTHOR_TOTAL_PRS);
            topAuthor = new AuthorDO.TopAuthor(name, totalPRCount);
        }
        resultSet.close();
        statement.close();
        connection.close();
        return topAuthor;
    }

    public AuthorDO.TopAuthor getTopAuthorWithDateInterval(int day) throws SQLException {
        AuthorDO.TopAuthor topAuthor = null;
        Connection connection = TransactionManager.getConnection();
        PreparedStatement preparedStmt = null;
        preparedStmt = connection.prepareStatement(SQL_GET_TOP_AUTHOR_PR_LIST_BY_DATE_INTERVAL);
        if(day == 30) {
            preparedStmt.setString(1, "30 DAY");
        }
        else if(day == 90){
            preparedStmt.setString(1, "90 DAY");

        }else if(day == 180){
            preparedStmt.setString(1, "180 DAY");
        }
        ResultSet resultSet = preparedStmt.executeQuery();
        connection.commit();
        while (resultSet.next()) {
            String name = resultSet.getString(DBConstants.PullRequest.PULL_REQUEST_AUTHOR_DISPLAY_NAME);
            int totalPRCount = resultSet.getInt("totalPRCount");
            topAuthor = new AuthorDO.TopAuthor(name, totalPRCount);
          
        }
        resultSet.close();
        connection.close();
        return topAuthor;
    }

    public AuthorDO.TopAuthor getTopAuthorWithDateIntervalAndState(int day, String state) throws SQLException {
        AuthorDO.TopAuthor topAuthor = null;
        Connection connection = TransactionManager.getConnection();
        PreparedStatement preparedStmt = null;
        preparedStmt = connection.prepareStatement(SQL_GET_TOP_AUTHOR_PR_LIST_BY_DATE_INTERVAL_AND_PR_STATE);
        preparedStmt.setString(1, state);
        if(day == 30) {
            preparedStmt.setString(2, "30 DAY");
        }
        else if(day == 90){
            preparedStmt.setString(2, "90 DAY");

        }else if(day == 180){
            preparedStmt.setString(2, "180 DAY");
        }
        ResultSet resultSet = preparedStmt.executeQuery();
        connection.commit();
        while (resultSet.next()) {
            String name = resultSet.getString(DBConstants.PullRequest.PULL_REQUEST_AUTHOR_DISPLAY_NAME);
            int totalPRCount = resultSet.getInt("totalPRCount");
            topAuthor = new AuthorDO.TopAuthor(name, totalPRCount);
          
        }
        resultSet.close();
        connection.close();
        return topAuthor;
    }


    

    public AuthorDO.TopAuthor getTopAuthorAtOpen() throws SQLException {
        Connection connection = TransactionManager.getConnection();
        AuthorDO.TopAuthor topAuthor = null;
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(SQL_GET_TOP_AUTHOR_BY_OPEN);
        while (resultSet.next()) {
            String name = resultSet.getString(DBConstants.Author.AUTHOR_NAME);
            int totalPRCount = resultSet.getInt(DBConstants.Author.AUTHOR_TOTAL_OPEN_PRS);
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
            String name = resultSet.getString(DBConstants.Author.AUTHOR_NAME);
            int totalPRCount = resultSet.getInt(DBConstants.Author.AUTHOR_TOTAL_MERGED_PRS);
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
            String name = resultSet.getString(DBConstants.Author.AUTHOR_NAME);
            int totalPRCount = resultSet.getInt(DBConstants.Author.AUTHOR_TOTAL_DECLINED_PRS);
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
            int id = resultSet.getInt(DBConstants.Author.AUTHOR_ID);
            String name = resultSet.getString(DBConstants.Author.AUTHOR_NAME);
            int total = resultSet.getInt(DBConstants.Author.AUTHOR_TOTAL_PRS);
            int merge = resultSet.getInt(DBConstants.Author.AUTHOR_TOTAL_MERGED_PRS);
            int open = resultSet.getInt(DBConstants.Author.AUTHOR_TOTAL_OPEN_PRS);
            int declined = resultSet.getInt(DBConstants.Author.AUTHOR_TOTAL_DECLINED_PRS);
            authors.add(new AuthorDO(id, name, total, merge, open, declined));
        }
        resultSet.close();
        statement.close();
        connection.close();
        return authors;
    }
    public ArrayList<AuthorDO> getAllAuthorsUpdateWithFilter(String startDate,String endDate) throws SQLException {
        ArrayList<AuthorDO> authors = new ArrayList<AuthorDO>();
        Connection connection = TransactionManager.getConnection();
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(SQL_GET_ALL_AUTHORS);
        while (resultSet.next()) {
            int id = resultSet.getInt(DBConstants.Author.AUTHOR_ID);
            String name = resultSet.getString(DBConstants.Author.AUTHOR_NAME);

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.ENGLISH);
            Date startDateFormat = Date.valueOf(LocalDate.parse(startDate, formatter));
            Date endDateFormat = Date.valueOf(LocalDate.parse(endDate, formatter));

            int merge = getAuthorUpdateTotalMergedPR(startDateFormat,endDateFormat,name);
            int open = getAuthorUpdateTotalOpenPR(startDateFormat,endDateFormat,name);
            int declined = getAuthorUpdateTotalDeclinedPR(startDateFormat,endDateFormat,name);

            int total = merge + open + declined;

            authors.add(new AuthorDO(id, name, total, merge, open, declined));
        }
        resultSet.close();
        statement.close();
        connection.close();
        return authors;
    }
    public int getAuthorUpdateTotalOpenPR(Date startDateFormat, Date endDateFormat, String name) throws SQLException{
        int updateOpenPR = 0;
        Connection connection = TransactionManager.getConnection();
        PreparedStatement preparedStmt = null;
        preparedStmt = connection.prepareStatement(SQL_GET_ALL_AUTHORS_UPDATE_WITH_FILTER_OPEN_PR);
        preparedStmt.setDate(1,startDateFormat);
        preparedStmt.setDate(2,endDateFormat);
        preparedStmt.setString(3,name);
        ResultSet resultSet = preparedStmt.executeQuery();
        connection.commit();
        while (resultSet.next()){
            int count = resultSet.getInt(DBConstants.Author.AUTHOR_UPDATE_FILTER);
             updateOpenPR = count;
        }
        preparedStmt.close();
        resultSet.close();
        connection.close();
        return updateOpenPR;
    }
    public int getAuthorUpdateTotalMergedPR(Date startDateFormat, Date endDateFormat, String name) throws SQLException{
        int updateMergedPR = 0;
        Connection connection = TransactionManager.getConnection();
        PreparedStatement preparedStmt = null;
        preparedStmt = connection.prepareStatement(SQL_GET_ALL_AUTHORS_UPDATE_WITH_FILTER_MERGED_PR);
        preparedStmt.setDate(1,startDateFormat);
        preparedStmt.setDate(2,endDateFormat);
        preparedStmt.setString(3,name);
        ResultSet resultSet = preparedStmt.executeQuery();
        connection.commit();
        while (resultSet.next()){
            int count = resultSet.getInt(DBConstants.Author.AUTHOR_UPDATE_FILTER);
            updateMergedPR = count;
        }
        preparedStmt.close();
        resultSet.close();
        connection.close();

       return updateMergedPR;
    }
    public int getAuthorUpdateTotalDeclinedPR(Date startDateFormat, Date endDateFormat, String name) throws SQLException{
        int updateDeclineddPR = 0;
        Connection connection = TransactionManager.getConnection();
        PreparedStatement preparedStmt = null;
        preparedStmt = connection.prepareStatement(SQL_GET_ALL_AUTHORS_UPDATE_WITH_FILTER_DECLINED_PR);
        preparedStmt.setDate(1,startDateFormat);
        preparedStmt.setDate(2,endDateFormat);
        preparedStmt.setString(3,name);
        ResultSet resultSet = preparedStmt.executeQuery();
        connection.commit();
        while (resultSet.next()){
            int count = resultSet.getInt(DBConstants.Author.AUTHOR_UPDATE_FILTER);
            updateDeclineddPR = count;
        }
        preparedStmt.close();
        resultSet.close();
        connection.close();

        return updateDeclineddPR;
    }
}