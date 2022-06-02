package com.orion.bitbucket.Bitbucket.service;

import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import com.orion.bitbucket.Bitbucket.dbc.DBConstants;
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
    private final String SQL_GET_PULL_REQUEST_BY_DATE_INTERVAL = "select * from pullrequest where state=? and display_name=? and  DATE(created_date) between ? and  ? ";


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
            int id = resultSet.getInt(DBConstants.PullRequest.PULL_REQUEST_ID);
            String title = resultSet.getString(DBConstants.PullRequest.PULL_REQUEST_TITLE);
            String state = resultSet.getString(DBConstants.PullRequest.PULL_REQUEST_STATE);
            boolean closed = resultSet.getBoolean(DBConstants.PullRequest.PULL_REQUEST_CLOSED);
            String description = resultSet.getString(DBConstants.PullRequest.PULL_REQUEST_DESCRIPTION);
            String updatedDate = resultSet.getString(DBConstants.PullRequest.PULL_REQUEST_UPDATE_DATE); // long olsun. db ona gore duzeltilsin
            Date createdDate = resultSet.getDate(DBConstants.PullRequest.PULL_REQUEST_CREATED_DATE); 
            Date closedDate = resultSet.getDate(DBConstants.PullRequest.PULL_REQUEST_CLOSED_DATE); 
            String emailAddress = resultSet.getString(DBConstants.PullRequest.PULL_REQUEST_AUTHOR_EMAIL_ADDRESS);
            String displayName = resultSet.getString(DBConstants.PullRequest.PULL_REQUEST_AUTHOR_DISPLAY_NAME);
            String slug = resultSet.getString(DBConstants.PullRequest.PULL_REQUEST_AUTHOR_SLUG);
            
            int indexOf = title.indexOf(DBConstants.PullRequest.PULL_REQUEST_JIRA_ID);
            String jiraId = null;
            if(indexOf > -1) {
                int starting = title.indexOf(DBConstants.PullRequest.PULL_REQUEST_JIRA_ID);
                jiraId = title.substring(starting, starting+9);
            }
            else {
                jiraId = DBConstants.PullRequest.PULL_REQUEST_NO_JIRA_ID;
            }
            list.add(new PullRequestDO(id, title, jiraId, state, closed, description, updatedDate, createdDate, closedDate, emailAddress, displayName, slug, null));
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
            int id = resultSet.getInt(DBConstants.PullRequest.PULL_REQUEST_ID);
            String title = resultSet.getString(DBConstants.PullRequest.PULL_REQUEST_TITLE);
            String state = resultSet.getString(DBConstants.PullRequest.PULL_REQUEST_STATE);
            boolean closed = resultSet.getBoolean(DBConstants.PullRequest.PULL_REQUEST_CLOSED);
            String description = resultSet.getString(DBConstants.PullRequest.PULL_REQUEST_DESCRIPTION);
            String updatedDate = resultSet.getString(DBConstants.PullRequest.PULL_REQUEST_UPDATE_DATE); // long olsun. db ona gore duzeltilsin
            Date createdDate = resultSet.getDate(DBConstants.PullRequest.PULL_REQUEST_CREATED_DATE); 
            Date closedDate = resultSet.getDate(DBConstants.PullRequest.PULL_REQUEST_CLOSED_DATE); 
            String emailAddress = resultSet.getString(DBConstants.PullRequest.PULL_REQUEST_AUTHOR_EMAIL_ADDRESS);
            String displayName = resultSet.getString(DBConstants.PullRequest.PULL_REQUEST_AUTHOR_DISPLAY_NAME);
            String slug = resultSet.getString(DBConstants.PullRequest.PULL_REQUEST_AUTHOR_SLUG);
            
            int indexOf = title.indexOf(DBConstants.PullRequest.PULL_REQUEST_JIRA_ID);
            String jiraId = null;
            if(indexOf > -1) {
                int starting = title.indexOf(DBConstants.PullRequest.PULL_REQUEST_JIRA_ID);
                jiraId = title.substring(starting, starting+9);
            }
            else {
                jiraId = DBConstants.PullRequest.PULL_REQUEST_NO_JIRA_ID;
            }
           
            list.add(new PullRequestDO(id, title, jiraId, state, closed, description, updatedDate, createdDate, closedDate, emailAddress, displayName, slug, null));
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
            int id = resultSet.getInt(DBConstants.PullRequest.PULL_REQUEST_ID);
            String title = resultSet.getString(DBConstants.PullRequest.PULL_REQUEST_TITLE);
            String state = resultSet.getString(DBConstants.PullRequest.PULL_REQUEST_STATE);
            boolean closed = resultSet.getBoolean(DBConstants.PullRequest.PULL_REQUEST_CLOSED);
            String description = resultSet.getString(DBConstants.PullRequest.PULL_REQUEST_DESCRIPTION);
            String updatedDate = resultSet.getString(DBConstants.PullRequest.PULL_REQUEST_UPDATE_DATE); // long olsun. db ona gore duzeltilsin
            Date createdDate = resultSet.getDate(DBConstants.PullRequest.PULL_REQUEST_CREATED_DATE); 
            Date closedDate = resultSet.getDate(DBConstants.PullRequest.PULL_REQUEST_CLOSED_DATE); 
            String emailAddress = resultSet.getString(DBConstants.PullRequest.PULL_REQUEST_AUTHOR_EMAIL_ADDRESS);
            String displayName = resultSet.getString(DBConstants.PullRequest.PULL_REQUEST_AUTHOR_DISPLAY_NAME);
            String slug = resultSet.getString(DBConstants.PullRequest.PULL_REQUEST_AUTHOR_SLUG);
            
            int indexOf = title.indexOf(DBConstants.PullRequest.PULL_REQUEST_JIRA_ID);
            String jiraId = null;
            if(indexOf > -1) {
                int starting = title.indexOf(DBConstants.PullRequest.PULL_REQUEST_JIRA_ID);
                jiraId = title.substring(starting, starting+9);
            }
            else {
               jiraId = DBConstants.PullRequest.PULL_REQUEST_NO_JIRA_ID;
            }
            pullRequest = new PullRequestDO(id, title, jiraId, state, closed, description, updatedDate, createdDate, closedDate, emailAddress, displayName, slug, null);
        }
        resultSet.close();
        preparedStmt.close();
        connection.close();
        return pullRequest;
    }


    // TODO : It gives an error
    public ArrayList<PullRequestDO> getPRListByStateAndUsernameAndDateInterval(String status, String username,
            String startDate, String endDate) throws SQLException, ParseException {
                

                Date date1=(Date) new SimpleDateFormat("yyyy-dd-mm").parse(startDate);  
                Date date2=(Date) new SimpleDateFormat("yyyy-dd-mm").parse(endDate);  


                ArrayList<PullRequestDO> list = new ArrayList<PullRequestDO>();
                Connection connection = TransactionManager.getConnection();
                PreparedStatement preparedStmt = null;
                preparedStmt = connection.prepareStatement(SQL_GET_PULL_REQUEST_BY_DATE_INTERVAL);
                preparedStmt.setString(1, status);
                preparedStmt.setString(2, username);
                preparedStmt.setDate(3,date1);
                preparedStmt.setDate(4, date2);
                ResultSet resultSet = preparedStmt.executeQuery();
                connection.commit();
                while (resultSet.next()) {
                    int id = resultSet.getInt(DBConstants.PullRequest.PULL_REQUEST_ID);
                    String title = resultSet.getString(DBConstants.PullRequest.PULL_REQUEST_TITLE);
                    String state = resultSet.getString(DBConstants.PullRequest.PULL_REQUEST_STATE);
                    boolean closed = resultSet.getBoolean(DBConstants.PullRequest.PULL_REQUEST_CLOSED);
                    String description = resultSet.getString(DBConstants.PullRequest.PULL_REQUEST_DESCRIPTION);
                    String updatedDate = resultSet.getString(DBConstants.PullRequest.PULL_REQUEST_UPDATE_DATE); // long olsun. db ona gore duzeltilsin
                    Date createdDate = resultSet.getDate(DBConstants.PullRequest.PULL_REQUEST_CREATED_DATE); 
                    Date closedDate = resultSet.getDate(DBConstants.PullRequest.PULL_REQUEST_CLOSED_DATE); 
                    String emailAddress = resultSet.getString(DBConstants.PullRequest.PULL_REQUEST_AUTHOR_EMAIL_ADDRESS);
                    String displayName = resultSet.getString(DBConstants.PullRequest.PULL_REQUEST_AUTHOR_DISPLAY_NAME);
                    String slug = resultSet.getString(DBConstants.PullRequest.PULL_REQUEST_AUTHOR_SLUG);
                    int indexOf = title.indexOf(DBConstants.PullRequest.PULL_REQUEST_JIRA_ID);
                    String jiraId = null;
                    if(indexOf > -1) {
                        int starting = title.indexOf(DBConstants.PullRequest.PULL_REQUEST_JIRA_ID);
                        jiraId = title.substring(starting, starting+9);
                    }
                    else {
                        jiraId = DBConstants.PullRequest.PULL_REQUEST_NO_JIRA_ID;
                    }
                   
                    list.add(new PullRequestDO(id, title, jiraId, state, closed, description, updatedDate, createdDate, closedDate, emailAddress, displayName, slug, null));
                }
                resultSet.close();
                preparedStmt.close();
                connection.close();
                return list;
      
    }

}