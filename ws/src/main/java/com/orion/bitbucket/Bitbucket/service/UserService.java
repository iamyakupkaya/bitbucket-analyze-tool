package com.orion.bitbucket.Bitbucket.service;

import com.orion.bitbucket.Bitbucket.dbc.DBConstants;
import com.orion.bitbucket.Bitbucket.dbc.TransactionManager;
import com.orion.bitbucket.Bitbucket.log.Log;
import com.orion.bitbucket.Bitbucket.model.UserDO;
import org.springframework.stereotype.Service;
import java.sql.*;
import java.util.*;

@Service
public class UserService extends BaseService implements UserServiceIF{
    private final boolean IS_USER_LOGGING = false;
    private static int COUNTER = 1000;
    private final String SQL_GET_USER_MAX_COUNTER_ID = "select max(id) from users ";
    private final String SQL_INSERT_USER = "insert into users(id,user_name,first_name,last_name,password,email_address,team_code,role) values (?,?,?,?,?,?,?,?)";
    private final String SQL_DELETE_USER_WITH_USER_NAME =  "delete from users where user_name = ?";
    private final String SQL_UPDATE_USER_INFORMATION = "update users set first_name = ?, last_name = ?, email_address = ? where user_name =?";
    private final String SQL_ALL_USERS = "select * from users";
    private final String SQL_GET_ALL_USERS_WITH_ROLE = "select * from users where role = ? ";
    private final String SQL_GET_FIRSTNAME_AND_LASTNAME_WITH_USERNAME = "select first_name, last_name from users where user_name = ?";
    private final String SQL_GET_USER_INFORMATION_WITH_USERNAME = "select * from users where user_name= ?";
    private final String SQL_GET_USER_COUNT_TOTAL_PULL_REQUEST = "select count(*) from pullrequest where slug = ?";
    private final String SQL_GET_USER_COUNT_TOTAL_REVIEW = "select count(*) from review where display_name = ?";
    private final String SQL_GET_CHECK_SAME_USERNAME = "select user_name from users where user_name = ?";
    private final String SQL_GET_USERS_WITH_TEAM_AND_ROLE = "select * from users where role = ? and team_Code = ?";
    private final String SQL_GET_USERS_WITH_TEAM_CODE= "select * from users where team_Code = ? ";
    private final String SQL_GET_ALL_PULLREQUEST_AUTHORS = "select display_name,email_address,slug from pullrequest group by display_name,email_address,slug order by slug ";

    public ArrayList<UserDO> getAllUsers() throws SQLException {
        ArrayList<UserDO> users = null;
        ResultSet resultSet = null;
        Statement statement = null;
        Connection connection = null;
        try {
            users = new ArrayList<UserDO>();
            connection = TransactionManager.getConnection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery(SQL_ALL_USERS);
            while (resultSet.next()) {
                int id = resultSet.getInt(DBConstants.User.USER_ID);
                String username = resultSet.getString(DBConstants.User.USER_NAME);
                String firstname = resultSet.getString(DBConstants.User.USER_FIRST_NAME);
                String lastname = resultSet.getString(DBConstants.User.USER_LAST_NAME);
                String password = resultSet.getString(DBConstants.User.USER_PASSWORD);
                String email = resultSet.getString(DBConstants.User.USER_EMAIL_ADDRESS);
                String teamCode = resultSet.getString(DBConstants.User.USER_TEAM_CODE);
                String role = resultSet.getString(DBConstants.User.USER_ROLE);
                users.add(new UserDO(id, username, firstname, lastname, password, email, teamCode, role));
            }
        } catch (Exception exception) {
            if (IS_USER_LOGGING){
                Log.logger(Log.LogConstant.TAG_WARN,String.valueOf(exception));}
        } finally {
            resultSet.close();
            statement.close();
            connection.close();
        }
        return users;
    }
    public ArrayList<UserDO> getAllUserWithRole(String Role) throws SQLException {
        ArrayList<UserDO> users = null;
        Connection connection = null;
        PreparedStatement preparedStmt = null;
        ResultSet resultSet = null;
        try {
            users = new ArrayList<UserDO>();
            connection = TransactionManager.getConnection();
            preparedStmt = connection.prepareStatement(SQL_GET_ALL_USERS_WITH_ROLE);
            preparedStmt.setString(1, Role);
            resultSet = preparedStmt.executeQuery();
            connection.commit();
            while (resultSet.next()) {
                int id = resultSet.getInt(DBConstants.User.USER_ID);
                String username = resultSet.getString(DBConstants.User.USER_NAME);
                String firstname = resultSet.getString(DBConstants.User.USER_FIRST_NAME);
                String lastname = resultSet.getString(DBConstants.User.USER_LAST_NAME);
                String password = resultSet.getString(DBConstants.User.USER_PASSWORD);
                String email = resultSet.getString(DBConstants.User.USER_EMAIL_ADDRESS);
                String teamCode = resultSet.getString(DBConstants.User.USER_TEAM_CODE);
                String role = resultSet.getString(DBConstants.User.USER_ROLE);
                users.add(new UserDO(id, username, firstname, lastname, password, email, teamCode, role));
            }
        } catch (Exception exception) {
            if (IS_USER_LOGGING) {
                Log.logger(Log.LogConstant.TAG_WARN, String.valueOf(exception));}
        } finally {
            resultSet.close();
            preparedStmt.close();
            connection.close();
        }
        return users;
    }

    public void getCollectUserInformation(String userName,String firstName,String lastName,
                                          String password,String email, String teamCode, String role)throws SQLException {
        ResultSet resultSet = null;
        PreparedStatement preparedStmt = null;
        Connection connection = null;
        try {
            String checkUserName = null;
            connection = TransactionManager.getConnection();
            preparedStmt = connection.prepareStatement(SQL_GET_CHECK_SAME_USERNAME);
            preparedStmt.setString(1, userName);
            resultSet = preparedStmt.executeQuery();
            connection.commit();
            while (resultSet.next()) {
                String username = resultSet.getString(DBConstants.User.USER_NAME);
                checkUserName = username;
            }
            COUNTER = maxUserID();
            if (userName.equals(checkUserName)) {
                Log.logger(Log.LogConstant.TAG_INFO, "has same username");
            } else {
                insertUser(COUNTER, userName, firstName, lastName, password, email, teamCode, role);
            }
        } catch (Exception exception) {
            if (IS_USER_LOGGING) {
                Log.logger(Log.LogConstant.TAG_WARN, String.valueOf(exception));}
        } finally {
            resultSet.close();
            preparedStmt.close();
            connection.close();
        }
    }
    public int maxUserID() throws SQLException {
        int maxUserId = 0;
        ResultSet resultSet = null;
        Statement statement = null;
        Connection connection = null;
        try {
            connection = TransactionManager.getConnection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery(SQL_GET_USER_MAX_COUNTER_ID);
            while (resultSet.next()) {
                int maxId = resultSet.getInt(DBConstants.User.USER_COUNTER_MAX_ID);
                maxUserId = (maxId + 1);
            }
        } catch (Exception exception) {
            if (IS_USER_LOGGING) {
                Log.logger(Log.LogConstant.TAG_WARN, String.valueOf(exception));
            }
        } finally {
            resultSet.close();
            statement.close();
            connection.close();
        }
        return maxUserId;
    }

    public void insertUser(int id,String userName,String firstName,String lastName,
                               String password,String email, String teamCode, String role) throws SQLException {
        Connection connection = null;
        PreparedStatement preparedStmt = null;
        try {
            connection = TransactionManager.getConnection();
            preparedStmt = connection.prepareStatement(SQL_INSERT_USER);
            preparedStmt.setInt(1, id);
            preparedStmt.setString(2, userName);
            preparedStmt.setString(3, firstName);
            preparedStmt.setString(4, lastName);
            preparedStmt.setString(5, password);
            preparedStmt.setString(6, email);
            preparedStmt.setString(7, teamCode);
            preparedStmt.setString(8, role);
            int row = preparedStmt.executeUpdate();
            connection.commit();
            COUNTER++;
        } catch (Exception exception) {
            if (IS_USER_LOGGING) {
                Log.logger(Log.LogConstant.TAG_WARN, String.valueOf(exception));
            }
        } finally {
            preparedStmt.close();
            connection.close();
        }
    }

    public ArrayList<String> getUserFirstAndLastName(String username)throws SQLException {
        ArrayList<String> list = null;
        Connection connection = null;
        PreparedStatement preparedStmt = null;
        ResultSet resultSet = null;
        try {
            list = new ArrayList<String>();
            connection = TransactionManager.getConnection();
            preparedStmt = connection.prepareStatement(SQL_GET_FIRSTNAME_AND_LASTNAME_WITH_USERNAME);
            preparedStmt.setString(1, username);
            resultSet = preparedStmt.executeQuery();
            connection.commit();
            while (resultSet.next()) {
                String firstname = resultSet.getString(DBConstants.User.USER_FIRST_NAME);
                String lastname = resultSet.getString(DBConstants.User.USER_LAST_NAME);
                list.add(firstname);
                list.add(lastname);
            }
        } catch (Exception exception) {
            if (IS_USER_LOGGING) {
                Log.logger(Log.LogConstant.TAG_WARN, String.valueOf(exception));
            }
        } finally {
            resultSet.close();
            preparedStmt.close();
            connection.close();
        }
        return list;
    }

    public UserDO getUserInformation(String username)throws SQLException {
        UserDO user = null;
        Connection connection = null;
        PreparedStatement preparedStmt = null;
        ResultSet resultSet = null;
        try {
            connection = TransactionManager.getConnection();
            preparedStmt = connection.prepareStatement(SQL_GET_USER_INFORMATION_WITH_USERNAME);
            preparedStmt.setString(1, username);
            resultSet = preparedStmt.executeQuery();
            connection.commit();
            while (resultSet.next()) {
                int id = resultSet.getInt(DBConstants.User.USER_ID);
                String userName = resultSet.getString(DBConstants.User.USER_NAME);
                String firstname = resultSet.getString(DBConstants.User.USER_FIRST_NAME);
                String lastname = resultSet.getString(DBConstants.User.USER_LAST_NAME);
                String password = resultSet.getString(DBConstants.User.USER_PASSWORD);
                String email = resultSet.getString(DBConstants.User.USER_EMAIL_ADDRESS);
                String teamCode = resultSet.getString(DBConstants.User.USER_TEAM_CODE);
                String role = resultSet.getString(DBConstants.User.USER_ROLE);
                user = new UserDO(id, userName, firstname, lastname, password, email, teamCode, role);
            }
        } catch (Exception exception) {
            if (IS_USER_LOGGING) {
                Log.logger(Log.LogConstant.TAG_WARN, String.valueOf(exception));
            }
        } finally {
            resultSet.close();
            preparedStmt.close();
            connection.close();
        }
        return user;
    }
    public void getDeleteUserWithUserName(String username)throws SQLException {
        Connection connection = null;
        PreparedStatement preparedStmt = null;
        try {
            connection = TransactionManager.getConnection();
            preparedStmt = connection.prepareStatement(SQL_DELETE_USER_WITH_USER_NAME);
            preparedStmt.setString(1, username);
            preparedStmt.executeUpdate();
            connection.commit();
        } catch (Exception exception) {
            if (IS_USER_LOGGING) {
                Log.logger(Log.LogConstant.TAG_WARN, String.valueOf(exception));
            }
        } finally {
            preparedStmt.close();
            connection.close();
        }
    }
    public void getUpdateUserInformation(String firstname,String lastname, String email, String username) throws SQLException {
        Connection connection = null;
        PreparedStatement preparedStmt = null;
        try {
            connection = TransactionManager.getConnection();
            preparedStmt = connection.prepareStatement(SQL_UPDATE_USER_INFORMATION);
            preparedStmt.setString(1, firstname);
            preparedStmt.setString(2, lastname);
            preparedStmt.setString(3, email);
            preparedStmt.setString(4, username);
            preparedStmt.executeUpdate();
            connection.commit();
        } catch (Exception exception) {
            if (IS_USER_LOGGING) {
                Log.logger(Log.LogConstant.TAG_WARN, String.valueOf(exception));
            }
        } finally {
            preparedStmt.close();
            connection.close();
        }
    }
    public int getUserCountTotalPR(String username)throws SQLException {
        int count = 0;
        Connection connection = null;
        PreparedStatement preparedStmt = null;
        ResultSet resultSet = null;
        try {
            connection = TransactionManager.getConnection();
            preparedStmt = connection.prepareStatement(SQL_GET_USER_COUNT_TOTAL_PULL_REQUEST);
            preparedStmt.setString(1, username);
            resultSet = preparedStmt.executeQuery();
            connection.commit();
            while (resultSet.next()) {
                count = resultSet.getInt(DBConstants.PullRequest.PULL_REQUEST_COUNT_BY_STATE);
            }
        } catch (Exception exception) {
            if (IS_USER_LOGGING) {
                Log.logger(Log.LogConstant.TAG_WARN, String.valueOf(exception));
            }
        } finally {
            resultSet.close();
            preparedStmt.close();
            connection.close();
        }
        return count;
    }
    public int getUserCountReview(String name)throws SQLException {
        int count = 0;
        Connection connection = null;
        PreparedStatement preparedStmt = null;
        ResultSet resultSet = null;
        try {
            connection = TransactionManager.getConnection();
            preparedStmt = connection.prepareStatement(SQL_GET_USER_COUNT_TOTAL_REVIEW);
            preparedStmt.setString(1, name);
            resultSet = preparedStmt.executeQuery();
            connection.commit();
            while (resultSet.next()) {
                count = resultSet.getInt(DBConstants.PullRequest.PULL_REQUEST_COUNT_BY_STATE);
            }
        } catch (Exception exception) {
            if (IS_USER_LOGGING) {
                Log.logger(Log.LogConstant.TAG_WARN, String.valueOf(exception));
            }
        } finally {
            resultSet.close();
            preparedStmt.close();
            connection.close();
        }
        return count;
    }
    public ArrayList<UserDO> getAllUserWithRoleAndTeam(String Role,String TeamCode) throws SQLException {
        ArrayList<UserDO> users = null;
        Connection connection = null;
        PreparedStatement preparedStmt = null;
        ResultSet resultSet = null;
        try {
            users = new ArrayList<UserDO>();
            connection = TransactionManager.getConnection();
            preparedStmt = connection.prepareStatement(SQL_GET_USERS_WITH_TEAM_AND_ROLE);
            preparedStmt.setString(1, Role);
            preparedStmt.setString(2, TeamCode);
            resultSet = preparedStmt.executeQuery();
            connection.commit();
            while (resultSet.next()) {
                int id = resultSet.getInt(DBConstants.User.USER_ID);
                String username = resultSet.getString(DBConstants.User.USER_NAME);
                String firstname = resultSet.getString(DBConstants.User.USER_FIRST_NAME);
                String lastname = resultSet.getString(DBConstants.User.USER_LAST_NAME);
                String password = resultSet.getString(DBConstants.User.USER_PASSWORD);
                String email = resultSet.getString(DBConstants.User.USER_EMAIL_ADDRESS);
                String teamCode = resultSet.getString(DBConstants.User.USER_TEAM_CODE);
                String role = resultSet.getString(DBConstants.User.USER_ROLE);
                users.add(new UserDO(id, username, firstname, lastname, password, email, teamCode, role));
            }
        } catch (Exception exception) {
            if (IS_USER_LOGGING) {
                Log.logger(Log.LogConstant.TAG_WARN, String.valueOf(exception));
            }
        } finally {
            resultSet.close();
            preparedStmt.close();
            connection.close();
        }
        return users;
    }
    public ArrayList<UserDO> getAllUserWithTeam(String TeamCode) throws SQLException {
        ArrayList<UserDO> users = null;
        Connection connection = null;
        PreparedStatement preparedStmt = null;
        ResultSet resultSet = null;
        try {
            users = new ArrayList<UserDO>();
            connection = TransactionManager.getConnection();
            preparedStmt = connection.prepareStatement(SQL_GET_USERS_WITH_TEAM_CODE);
            preparedStmt.setString(1, TeamCode);
            resultSet = preparedStmt.executeQuery();
            connection.commit();
            while (resultSet.next()) {
                int id = resultSet.getInt(DBConstants.User.USER_ID);
                String username = resultSet.getString(DBConstants.User.USER_NAME);
                String firstname = resultSet.getString(DBConstants.User.USER_FIRST_NAME);
                String lastname = resultSet.getString(DBConstants.User.USER_LAST_NAME);
                String password = resultSet.getString(DBConstants.User.USER_PASSWORD);
                String email = resultSet.getString(DBConstants.User.USER_EMAIL_ADDRESS);
                String teamCode = resultSet.getString(DBConstants.User.USER_TEAM_CODE);
                String role = resultSet.getString(DBConstants.User.USER_ROLE);
                users.add(new UserDO(id, username, firstname, lastname, password, email, teamCode, role));
            }
        } catch (Exception exception) {
            if (IS_USER_LOGGING) {
                Log.logger(Log.LogConstant.TAG_WARN, String.valueOf(exception));
            }
        } finally {
            resultSet.close();
            preparedStmt.close();
            connection.close();
        }
        return users;
    }
    public void insertUserTable() throws SQLException{
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        String firstname = null;
        String lastname = null;
        try {
            connection = TransactionManager.getConnection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery(SQL_GET_ALL_PULLREQUEST_AUTHORS);
            while (resultSet.next()) {
                String displayName = resultSet.getString(DBConstants.PullRequest.PULL_REQUEST_AUTHOR_DISPLAY_NAME);
                String email = resultSet.getString(DBConstants.PullRequest.PULL_REQUEST_AUTHOR_EMAIL_ADDRESS);
                String slug = resultSet.getString(DBConstants.PullRequest.PULL_REQUEST_AUTHOR_SLUG);

                if(displayName.contains(",")){
                    String[] parts = displayName.split(",");
                     lastname = parts[0];
                     firstname = parts[1].substring(1,(parts[1].length()));
                }else{
                    firstname = displayName;
                    lastname = "";
                }
                getCollectUserInformation(slug,firstname,lastname,DBConstants.User.DEFAULT_USER_PASSWORD,email,
                        DBConstants.User.DEFAULT_USERS_TEAM_EMPTY,DBConstants.User.USER_ROLE_USER);
            }
        }catch (Exception exception){
            if (IS_USER_LOGGING){Log.logger(Log.LogConstant.TAG_WARN,String.valueOf(exception));}
        }finally {
            resultSet.close();
            statement.close();
            connection.close();
        }
    }
    public List<String> getRoles() throws SQLException{
        List<String> roles = new ArrayList<String>();
        roles.add(DBConstants.User.USER_ROLE_LEADER);
        roles.add(DBConstants.User.USER_ROLE_USER);
        return roles;
    }
}