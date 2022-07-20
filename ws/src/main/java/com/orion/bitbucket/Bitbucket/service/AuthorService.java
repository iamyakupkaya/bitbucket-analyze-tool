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
    private final boolean IS_AUTHOR_LOGGING = false;
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
    private final String SQL_GET_ALL_AUTHORS_UPDATE_WITH_FILTER_CLOSED_DATE = "select count(state) as count from (select * from pullrequest where display_name= ? and DATE(closed_date) between ? and  ? )as count where state= ?" ;
    private final String SQL_GET_ALL_AUTHORS_UPDATE_WITH_FILTER_CREATED_DATE = "select count(state) as count from (select * from pullrequest where display_name= ? and DATE(created_date) between ? and  ? )as count where state= ?" ;
    private final String SQL_GET_AUTHORS_UPDATE = "update author set total_prs = ?, total_merged_prs = ?, total_open_prs = ?, total_declined_prs = ? where name = ?";
    public int getAuthorCount() throws SQLException {
        int count = 0;
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            connection = TransactionManager.getConnection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery(SQL_GET_AUTHOR_COUNT);
            while (resultSet.next()) {
                count = resultSet.getInt("count");
            }
        } catch (Exception exception) {
            if (IS_AUTHOR_LOGGING) {
                Log.logger(Log.LogConstant.TAG_WARN, String.valueOf(exception));
            }
        } finally {
            resultSet.close();
            statement.close();
            connection.close();
        }
        return count;
    }
    public ArrayList<String> getAllAuthor() throws SQLException {
        ArrayList<String> authors = new ArrayList<String>();
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            connection = TransactionManager.getConnection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery(SQL_GET_ALL_AUTHOR);
            while (resultSet.next()) {
                String displayName = resultSet.getString(DBConstants.PullRequest.PULL_REQUEST_AUTHOR_DISPLAY_NAME);
                authors.add(displayName);
            }
            if (isAuthorTableEmpty()) {
                for (int i = 0; i < authors.size(); i++) {
                    insertAuthorData(authors.get(i));
                }
            }
        } catch (Exception exception) {
            if (IS_AUTHOR_LOGGING) {
                Log.logger(Log.LogConstant.TAG_WARN, String.valueOf(exception));}
        } finally {
            resultSet.close();
            statement.close();
            connection.close();
        }
        return authors;
    }
    public boolean isAuthorTableEmpty() throws SQLException {
        int count = 0;
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            connection = TransactionManager.getConnection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery(SQL_IS_AUTHOR_TABLE_EMPTY);
            while (resultSet.next()) {
                count = resultSet.getInt("count");
            }
        } catch (Exception exception) {
            if (IS_AUTHOR_LOGGING) {
                Log.logger(Log.LogConstant.TAG_WARN, String.valueOf(exception));}
        } finally {
            resultSet.close();
            statement.close();
            connection.close();
        }
        return count > 0 ? false : true;
    }
    public int getCountPRByStateAndUsername(String state, String username) throws SQLException {
        int count = 0;
        Connection connection = null;
        PreparedStatement preparedStmt = null;
        ResultSet resultSet = null;
        try {
            connection = TransactionManager.getConnection();
            preparedStmt = connection.prepareStatement(SQL_GET_COUNT_PR_BY_STATE_AND_USERNAME);
            preparedStmt.setString(1, state);
            preparedStmt.setString(2, username);
            resultSet = preparedStmt.executeQuery();
            connection.commit(); // ADDED
            while (resultSet.next()) {
                count = resultSet.getInt("count");
            }
        } catch (Exception exception) {
            if (IS_AUTHOR_LOGGING) {
                Log.logger(Log.LogConstant.TAG_WARN, String.valueOf(exception));}
        } finally {
            resultSet.close();
            preparedStmt.close();
            connection.close();
        }
        return count;
    }
    public ArrayList<AuthorDO> getCountPRStatesByUsername(String name) throws SQLException {

        ArrayList<AuthorDO> list = new ArrayList<>();
        Connection connection = null;
        PreparedStatement preparedStmt = null;
        ResultSet resultSet = null;
        try {
            connection = TransactionManager.getConnection();
            preparedStmt = connection.prepareStatement(SQL_GET_AUTHOR_BY_USERNAME);
            preparedStmt.setString(1, name);
            resultSet = preparedStmt.executeQuery();
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
        } catch (Exception exception) {
            if (IS_AUTHOR_LOGGING) {
                Log.logger(Log.LogConstant.TAG_WARN, String.valueOf(exception));}
        } finally {
            resultSet.close();
            preparedStmt.close();
            connection.close();
        }
        return list;
    }
    public void insertAuthorData(String author) throws SQLException {
        int totalMergedPRs = getCountPRByStateAndUsername(DBConstants.PullRequestState.MERGED, author);
        int totalOpenPRs = getCountPRByStateAndUsername(DBConstants.PullRequestState.OPEN, author);
        int totalDeclinedPRs = getCountPRByStateAndUsername(DBConstants.PullRequestState.DECLINED, author);
        int totalPRs = totalMergedPRs + totalOpenPRs + totalDeclinedPRs;
        Connection connection = null;
        PreparedStatement preparedStmt = null;
        try {
            connection = TransactionManager.getConnection();
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
        } catch (Exception exception) {
            if (IS_AUTHOR_LOGGING) {
                Log.logger(Log.LogConstant.TAG_WARN, String.valueOf(exception));}
        } finally {
            preparedStmt.close();
            connection.close();
        }
    }

    public AuthorDO.TopAuthor getTopAuthor() throws SQLException {
        AuthorDO.TopAuthor topAuthor = null;
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            connection = TransactionManager.getConnection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery(SQL_GET_TOP_AUTHOR);
            while (resultSet.next()) {
                String name = resultSet.getString(DBConstants.Author.AUTHOR_NAME);
                int totalPRCount = resultSet.getInt(DBConstants.Author.AUTHOR_TOTAL_PRS);
                topAuthor = new AuthorDO.TopAuthor(name, totalPRCount);
            }
        } catch (Exception exception) {
            if (IS_AUTHOR_LOGGING) {
                Log.logger(Log.LogConstant.TAG_WARN, String.valueOf(exception));}
        } finally {
            resultSet.close();
            statement.close();
            connection.close();
        }
        return topAuthor;
    }
    public AuthorDO.TopAuthor getTopAuthorWithDateInterval(int day) throws SQLException {
        AuthorDO.TopAuthor topAuthor = null;
        Connection connection = null;
        PreparedStatement preparedStmt = null;
        ResultSet resultSet = null;
        try {
            connection = TransactionManager.getConnection();
            preparedStmt = connection.prepareStatement(SQL_GET_TOP_AUTHOR_PR_LIST_BY_DATE_INTERVAL);
            if (day == 30) {
                preparedStmt.setString(1, "30 DAY");
            } else if (day == 90) {
                preparedStmt.setString(1, "90 DAY");

            } else if (day == 180) {
                preparedStmt.setString(1, "180 DAY");
            }
            resultSet = preparedStmt.executeQuery();
            connection.commit();
            while (resultSet.next()) {
                String name = resultSet.getString(DBConstants.PullRequest.PULL_REQUEST_AUTHOR_DISPLAY_NAME);
                int totalPRCount = resultSet.getInt("totalPRCount");
                topAuthor = new AuthorDO.TopAuthor(name, totalPRCount);
            }
        } catch (Exception exception) {
            if (IS_AUTHOR_LOGGING) {
                Log.logger(Log.LogConstant.TAG_WARN, String.valueOf(exception));}
        } finally {
            resultSet.close();
            preparedStmt.close();
            connection.close();
        }
        return topAuthor;
    }
    public AuthorDO.TopAuthor getTopAuthorWithDateIntervalAndState(int day, String state) throws SQLException {
        AuthorDO.TopAuthor topAuthor = null;
        Connection connection = null;
        PreparedStatement preparedStmt = null;
        ResultSet resultSet = null;
        try {
            connection = TransactionManager.getConnection();

            preparedStmt = connection.prepareStatement(SQL_GET_TOP_AUTHOR_PR_LIST_BY_DATE_INTERVAL_AND_PR_STATE);
            preparedStmt.setString(1, state);
            if (day == 30) {
                preparedStmt.setString(2, "30 DAY");
            } else if (day == 90) {
                preparedStmt.setString(2, "90 DAY");

            } else if (day == 180) {
                preparedStmt.setString(2, "180 DAY");
            }
            resultSet = preparedStmt.executeQuery();
            connection.commit();
            while (resultSet.next()) {
                String name = resultSet.getString(DBConstants.PullRequest.PULL_REQUEST_AUTHOR_DISPLAY_NAME);
                int totalPRCount = resultSet.getInt("totalPRCount");
                topAuthor = new AuthorDO.TopAuthor(name, totalPRCount);

            }
        } catch (Exception exception) {
            if (IS_AUTHOR_LOGGING) {
                Log.logger(Log.LogConstant.TAG_WARN, String.valueOf(exception));
            }
        } finally {
            resultSet.close();
            preparedStmt.close();
            connection.close();
        }
        return topAuthor;
    }


    public AuthorDO.TopAuthor getTopAuthorAtOpen() throws SQLException {
        AuthorDO.TopAuthor topAuthor = null;
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            connection = TransactionManager.getConnection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery(SQL_GET_TOP_AUTHOR_BY_OPEN);
            while (resultSet.next()) {
                String name = resultSet.getString(DBConstants.Author.AUTHOR_NAME);
                int totalPRCount = resultSet.getInt(DBConstants.Author.AUTHOR_TOTAL_OPEN_PRS);
                topAuthor = new AuthorDO.TopAuthor(name, totalPRCount);
            }
        } catch (Exception exception) {
            if (IS_AUTHOR_LOGGING) {
                Log.logger(Log.LogConstant.TAG_WARN, String.valueOf(exception));
            }
        } finally {
            resultSet.close();
            statement.close();
            connection.close();
        }
        return topAuthor;
    }

    public AuthorDO.TopAuthor getTopAuthorAtMerged() throws SQLException {
        AuthorDO.TopAuthor topAuthor = null;
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            connection = TransactionManager.getConnection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery(SQL_GET_TOP_AUTHOR_BY_MERGED);
            while (resultSet.next()) {
                String name = resultSet.getString(DBConstants.Author.AUTHOR_NAME);
                int totalPRCount = resultSet.getInt(DBConstants.Author.AUTHOR_TOTAL_MERGED_PRS);
                topAuthor = new AuthorDO.TopAuthor(name, totalPRCount);
            }
        } catch (Exception exception) {
            if (IS_AUTHOR_LOGGING) {
                Log.logger(Log.LogConstant.TAG_WARN, String.valueOf(exception));}
        } finally {
            resultSet.close();
            statement.close();
            connection.close();
        }
        return topAuthor;
    }
    public AuthorDO.TopAuthor getTopAuthorAtDeclined() throws SQLException {
        AuthorDO.TopAuthor topAuthor = null;
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            connection = TransactionManager.getConnection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery(SQL_GET_TOP_AUTHOR_BY_DECLINED);
            while (resultSet.next()) {
                String name = resultSet.getString(DBConstants.Author.AUTHOR_NAME);
                int totalPRCount = resultSet.getInt(DBConstants.Author.AUTHOR_TOTAL_DECLINED_PRS);
                topAuthor = new AuthorDO.TopAuthor(name, totalPRCount);
            }
        } catch (Exception exception) {
            if (IS_AUTHOR_LOGGING) {
                Log.logger(Log.LogConstant.TAG_WARN, String.valueOf(exception));
            }
        } finally {
            resultSet.close();
            statement.close();
            connection.close();
        }
        return topAuthor;
    }

    public ArrayList<AuthorDO> getAllAuthors() throws SQLException {

        ArrayList<AuthorDO> authors = new ArrayList<AuthorDO>();
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            connection = TransactionManager.getConnection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery(SQL_GET_ALL_AUTHORS);
            while (resultSet.next()) {
                int id = resultSet.getInt(DBConstants.Author.AUTHOR_ID);
                String name = resultSet.getString(DBConstants.Author.AUTHOR_NAME);
                int total = resultSet.getInt(DBConstants.Author.AUTHOR_TOTAL_PRS);
                int merge = resultSet.getInt(DBConstants.Author.AUTHOR_TOTAL_MERGED_PRS);
                int open = resultSet.getInt(DBConstants.Author.AUTHOR_TOTAL_OPEN_PRS);
                int declined = resultSet.getInt(DBConstants.Author.AUTHOR_TOTAL_DECLINED_PRS);
                authors.add(new AuthorDO(id, name, total, merge, open, declined));
            }
        } catch (Exception exception) {
            if (IS_AUTHOR_LOGGING) {
                Log.logger(Log.LogConstant.TAG_WARN, String.valueOf(exception));
            }
        } finally {
            resultSet.close();
            statement.close();
            connection.close();
        }
        return authors;
    }
    public ArrayList<AuthorDO> getAllAuthorsUpdateWithFilter(String startDate,String endDate) throws SQLException {

        ArrayList<AuthorDO> authors = new ArrayList<AuthorDO>();
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            connection = TransactionManager.getConnection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery(SQL_GET_ALL_AUTHORS);
            while (resultSet.next()) {
                int id = resultSet.getInt(DBConstants.Author.AUTHOR_ID);
                String name = resultSet.getString(DBConstants.Author.AUTHOR_NAME);

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.ENGLISH);
                Date startDateFormat = Date.valueOf(LocalDate.parse(startDate, formatter));
                Date endDateFormat = Date.valueOf(LocalDate.parse(endDate, formatter));

                int merge = getAuthorUpdate(startDateFormat, endDateFormat,
                        name, DBConstants.PullRequestState.MERGED, SQL_GET_ALL_AUTHORS_UPDATE_WITH_FILTER_CLOSED_DATE);
                int declined = getAuthorUpdate(startDateFormat, endDateFormat,
                        name, DBConstants.PullRequestState.DECLINED, SQL_GET_ALL_AUTHORS_UPDATE_WITH_FILTER_CLOSED_DATE);
                int open = getAuthorUpdate(startDateFormat, endDateFormat,
                        name, DBConstants.PullRequestState.OPEN, SQL_GET_ALL_AUTHORS_UPDATE_WITH_FILTER_CREATED_DATE);
                int total = merge + open + declined;
                authors.add(new AuthorDO(id, name, total, merge, open, declined));
            }
        } catch (Exception exception) {
            if (IS_AUTHOR_LOGGING) {
                Log.logger(Log.LogConstant.TAG_WARN, String.valueOf(exception));}
        } finally {
            resultSet.close();
            statement.close();
            connection.close();
        }
        return authors;
    }
    public int getAuthorUpdate(Date startDateFormat, Date endDateFormat, String name,String state,String Query) throws SQLException {
        int count = 0;
        Connection connection = null;
        ResultSet resultSet = null;
        PreparedStatement preparedStmt = null;
        try {
            connection = TransactionManager.getConnection();
            preparedStmt = connection.prepareStatement(Query);
            preparedStmt.setString(1, name);
            preparedStmt.setDate(2, startDateFormat);
            preparedStmt.setDate(3, endDateFormat);
            preparedStmt.setString(4, state);
            resultSet = preparedStmt.executeQuery();
            connection.commit();
            while (resultSet.next()) {
                count = resultSet.getInt(DBConstants.Author.AUTHOR_UPDATE_FILTER);
            }
        } catch (Exception exception) {
            if (IS_AUTHOR_LOGGING) {
                Log.logger(Log.LogConstant.TAG_WARN, String.valueOf(exception));}
        } finally {
            preparedStmt.close();
            resultSet.close();
            connection.close();
        }
        return count;
    }
    public void getAuthorUpdateList(String name) throws SQLException {
        int totalMergedPRs = getCountPRByStateAndUsername(DBConstants.PullRequestState.MERGED, name);
        int totalOpenPRs = getCountPRByStateAndUsername(DBConstants.PullRequestState.OPEN, name);
        int totalDeclinedPRs = getCountPRByStateAndUsername(DBConstants.PullRequestState.DECLINED, name);
        int totalPRs = totalMergedPRs + totalOpenPRs + totalDeclinedPRs;
        Connection connection = null;
        PreparedStatement preparedStmt = null;
        try {
            connection = TransactionManager.getConnection();
            preparedStmt = connection.prepareStatement(SQL_GET_AUTHORS_UPDATE);
            preparedStmt.setInt(1,totalPRs);
            preparedStmt.setInt(2, totalMergedPRs);
            preparedStmt.setInt(3, totalOpenPRs);
            preparedStmt.setInt(4, totalDeclinedPRs);
            preparedStmt.setString(5, name);
            preparedStmt.executeUpdate();
            connection.commit();
        } catch (Exception exception) {
            if (IS_AUTHOR_LOGGING) {
                Log.logger(Log.LogConstant.TAG_WARN, String.valueOf(exception));}
        } finally {
            preparedStmt.close();
            connection.close();
        }
    }
}