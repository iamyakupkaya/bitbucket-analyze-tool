package com.orion.bitbucket.Bitbucket.service;

import com.orion.bitbucket.Bitbucket.dbc.DBConstants;
import com.orion.bitbucket.Bitbucket.dbc.TransactionManager;
import com.orion.bitbucket.Bitbucket.log.Log;
import com.orion.bitbucket.Bitbucket.model.UserDO;
import org.springframework.stereotype.Service;

import java.sql.*;
import java.util.ArrayList;

@Service
public class UserService extends BaseService implements UserServiceIF{
    private final boolean IS_USER_LOGGING = false;
    private static int COUNTER = 1000;
    private final String SQL_GET_USER_MAX_COUNTER_ID = "select max(id) from users ";
    private final String SQL_INSERT_USER = "insert into users(id,user_name,first_name,last_name,password,email_address,team_code,role) values (?,?,?,?,?,?,?,?)";
    private final String SQL_DELETE_USER_WITH_USER_NAME =  "delete from users where user_name = ?";
    private final String SQL_UPDATE_USER_WITH_USER_NAME = "update users set user_name = ?, first_name = ?, last_name = ?, password =?, email_address = ?,team_code =?,  role = ? where user_name =?";
    private final String SQL_ALL_USERS = "select * from users";
    private final String SQL_GET_ALL_USERS_WITH_ROLE = "select * from users where role = ? ";
    private final String SQL_GET_FIRSTNAME_AND_LASTNAME_WITH_USERNAME = "select first_name, last_name from users where user_name = ?";
    private final String SQL_GET_USER_INFORMATION_WITH_USERNAME = "select * from users where user_name= ?";
    private final String SQL_GET_USER_COUNT_TOTAL_PULL_REQUEST = "select count(*) from pullrequest where slug = ?";
    private final String SQL_GET_USER_COUNT_TOTAL_REVIEW = "select count(*) from review where display_name = ?";
    private final String SQL_GET_CHECK_SAME_USERNAME = "select user_name from users where user_name = ?";

    public ArrayList<UserDO> getAllUsers() throws SQLException{
        ArrayList<UserDO> users = new ArrayList<UserDO>();
        Connection connection = TransactionManager.getConnection();
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(SQL_ALL_USERS);
        while (resultSet.next()){
            int id = resultSet.getInt(DBConstants.User.USER_ID);
            String username = resultSet.getString(DBConstants.User.USER_NAME);
            String firstname = resultSet.getString(DBConstants.User.USER_FIRST_NAME);
            String lastname = resultSet.getString(DBConstants.User.USER_LAST_NAME);
            String password = resultSet.getString(DBConstants.User.USER_PASSWORD);
            String email = resultSet.getString(DBConstants.User.USER_EMAIL_ADDRESS);
            String teamCode = resultSet.getString(DBConstants.User.USER_TEAM_CODE);
            String role = resultSet.getString(DBConstants.User.USER_ROLE);
            users.add(new UserDO(id,username,firstname,lastname,password,email,teamCode,role));
        }
        resultSet.close();
        statement.close();
        connection.close();
        return users;
    }
    public ArrayList<UserDO> getAllUserWithRole(String Role) throws SQLException{
        ArrayList<UserDO> users = new ArrayList<UserDO>();
        Connection connection = TransactionManager.getConnection();
        PreparedStatement preparedStmt = null;
        preparedStmt = connection.prepareStatement(SQL_GET_ALL_USERS_WITH_ROLE);
        preparedStmt.setString(1,Role);
        ResultSet resultSet = preparedStmt.executeQuery();
        connection.commit();
        while (resultSet.next()){
            int id = resultSet.getInt(DBConstants.User.USER_ID);
            String username = resultSet.getString(DBConstants.User.USER_NAME);
            String firstname = resultSet.getString(DBConstants.User.USER_FIRST_NAME);
            String lastname = resultSet.getString(DBConstants.User.USER_LAST_NAME);
            String password = resultSet.getString(DBConstants.User.USER_PASSWORD);
            String email = resultSet.getString(DBConstants.User.USER_EMAIL_ADDRESS);
            String teamCode = resultSet.getString(DBConstants.User.USER_TEAM_CODE);
            String role = resultSet.getString(DBConstants.User.USER_ROLE);
            users.add(new UserDO(id,username,firstname,lastname,password,email,teamCode,role));
        }
        resultSet.close();
        preparedStmt.close();
        connection.close();
        return users;
    }

    public void getCollectUserInformation(String userName,String firstName,String lastName,
                                          String password,String email, String teamCode, String role)throws SQLException{
//        // Test Data
//        insertUser(COUNTER,"syagsagan","Sinan","YAGSAGAN","123456",
//                "sinan.yagsagan@orioninc.com","NRD1222",DBConstants.User.USER_ROLE_ADMIN);
//        insertUser(COUNTER,"ogulusoy","Oguzhan","ULUSOY","123456",
//                "oguzhan.ulusoy@orioninc.com","NRD1222",DBConstants.User.USER_ROLE_NORMAL);
//        insertUser(COUNTER,"rsaglam","Ridvan","SAGLAM","123456",
//                "ridvan.saglam@orioninc.com","NRD1222",DBConstants.User.USER_ROLE_LEADER);
        try{
            String checkUserName = null;
            Connection connection = TransactionManager.getConnection();
            PreparedStatement preparedStmt = null;
            preparedStmt = connection.prepareStatement(SQL_GET_CHECK_SAME_USERNAME);
            preparedStmt.setString(1,userName);
            ResultSet resultSet = preparedStmt.executeQuery();
            connection.commit();
            while (resultSet.next()){
                String username = resultSet.getString(DBConstants.User.USER_NAME);
                checkUserName = username;
            }
            resultSet.close();
            preparedStmt.close();
            connection.close();

            COUNTER = maxUserID();
            if(userName.equals(checkUserName)){
                Log.logger(Log.LogConstant.TAG_WARN,"has same username");
            }else{
                insertUser(COUNTER,userName,firstName,lastName,password,email,teamCode,role);
            }
        }catch (Exception e){}

    }
    public int maxUserID() throws SQLException {
        int maxUserId = 0;
        Connection connection = TransactionManager.getConnection();
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(SQL_GET_USER_MAX_COUNTER_ID);
        while (resultSet.next()){
            int maxId = resultSet.getInt(DBConstants.User.USER_COUNTER_MAX_ID);
            maxUserId = (maxId + 1);
        }
        resultSet.close();
        statement.close();
        connection.close();
        return maxUserId;
    }

    public void insertUser(int id,String userName,String firstName,String lastName,
                               String password,String email, String teamCode, String role) throws SQLException{

        Connection connection = TransactionManager.getConnection();
        try {
            PreparedStatement preparedStmt = null;
            preparedStmt = connection.prepareStatement(SQL_INSERT_USER);
            preparedStmt.setInt(1,id);
            preparedStmt.setString(2,userName);
            preparedStmt.setString(3,firstName);
            preparedStmt.setString(4,lastName);
            preparedStmt.setString(5,password);
            preparedStmt.setString(6,email);
            preparedStmt.setString(7,teamCode);
            preparedStmt.setString(8,role);
            int row = preparedStmt.executeUpdate();
            connection.commit();
            COUNTER++;
        }catch (Exception e){
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }finally {
            connection.close();
        }
    }

    public ArrayList<String> getUserFirstAndLastName(String username)throws SQLException{
        ArrayList<String> list = new ArrayList<String>();
        Connection connection = TransactionManager.getConnection();
        PreparedStatement preparedStmt = null;
        preparedStmt = connection.prepareStatement(SQL_GET_FIRSTNAME_AND_LASTNAME_WITH_USERNAME);
        preparedStmt.setString(1,username);
        ResultSet resultSet = preparedStmt.executeQuery();
        connection.commit();
        while (resultSet.next()){

            String firstname = resultSet.getString(DBConstants.User.USER_FIRST_NAME);
            String lastname = resultSet.getString(DBConstants.User.USER_LAST_NAME);
            list.add(firstname);
            list.add(lastname);
        }
        resultSet.close();
        preparedStmt.close();
        connection.close();
        return list;
    }

    public UserDO getUserInformation(String username)throws SQLException{
        UserDO user = null;
        Connection connection = TransactionManager.getConnection();
        PreparedStatement preparedStmt = null;
        preparedStmt = connection.prepareStatement(SQL_GET_USER_INFORMATION_WITH_USERNAME);
        preparedStmt.setString(1,username);
        ResultSet resultSet = preparedStmt.executeQuery();
        connection.commit();
        while (resultSet.next()){
            int id = resultSet.getInt(DBConstants.User.USER_ID);
            String userName = resultSet.getString(DBConstants.User.USER_NAME);
            String firstname = resultSet.getString(DBConstants.User.USER_FIRST_NAME);
            String lastname = resultSet.getString(DBConstants.User.USER_LAST_NAME);
            String password = resultSet.getString(DBConstants.User.USER_PASSWORD);
            String email = resultSet.getString(DBConstants.User.USER_EMAIL_ADDRESS);
            String teamCode = resultSet.getString(DBConstants.User.USER_TEAM_CODE);
            String role = resultSet.getString(DBConstants.User.USER_ROLE);
            user = new UserDO(id,userName,firstname,lastname,password,email,teamCode,role);
        }
        resultSet.close();
        preparedStmt.close();
        connection.close();
        return user;
    }
    public void getDeleteUserWithUserName(String username)throws SQLException{
        Connection connection = TransactionManager.getConnection();
        PreparedStatement preparedStmt = null;
        preparedStmt = connection.prepareStatement(SQL_DELETE_USER_WITH_USER_NAME);
        preparedStmt.setString(1,username);
        preparedStmt.executeUpdate();
        connection.commit();
        preparedStmt.close();
        connection.close();
    }

    public void getPreconditionForUpdate(String username,String firstname,String lastname,
                                         String password,String email, String teamCode, String role, String oldUsername)throws SQLException{
        try{
            String checkUserName = null;
            Connection connection = TransactionManager.getConnection();
            PreparedStatement preparedStmt = null;
            preparedStmt = connection.prepareStatement(SQL_GET_CHECK_SAME_USERNAME);
            preparedStmt.setString(1,username);
            ResultSet resultSet = preparedStmt.executeQuery();
            connection.commit();
            while (resultSet.next()){
                String userName = resultSet.getString(DBConstants.User.USER_NAME);
                checkUserName = userName;
            }
            resultSet.close();
            preparedStmt.close();
            connection.close();
            if(username.equals(checkUserName)){
                Log.logger(Log.LogConstant.TAG_WARN,"has same username");
            }else{
                getUpdateUserWithUserName(username,firstname,lastname,password,email,teamCode,role,oldUsername);
            }
        }catch (Exception e){}
    }
    public void getUpdateUserWithUserName(String username,String firstname,String lastname,
                              String password,String email, String teamCode, String role, String oldUsername) throws SQLException{

        Connection connection = TransactionManager.getConnection();
        PreparedStatement preparedStmt = null;
        preparedStmt = connection.prepareStatement(SQL_UPDATE_USER_WITH_USER_NAME);
        preparedStmt.setString(1,username);
        preparedStmt.setString(2,firstname);
        preparedStmt.setString(3,lastname);
        preparedStmt.setString(4,password);
        preparedStmt.setString(5,email);
        preparedStmt.setString(6,teamCode);
        preparedStmt.setString(7,role);
        preparedStmt.setString(8,oldUsername);
        preparedStmt.executeUpdate();

        connection.commit();
        preparedStmt.close();
        connection.close();
    }
    public int getUserCountTotalPR(String username)throws SQLException{
        int count = 0;
            Connection connection = TransactionManager.getConnection();
            PreparedStatement preparedStmt = null;
            preparedStmt = connection.prepareStatement(SQL_GET_USER_COUNT_TOTAL_PULL_REQUEST);
            preparedStmt.setString(1,username);
            ResultSet resultSet = preparedStmt.executeQuery();
            connection.commit();
            while (resultSet.next()){
                count = resultSet.getInt(DBConstants.PullRequest.PULL_REQUEST_COUNT_BY_STATE);
            }
            resultSet.close();
            preparedStmt.close();
            connection.close();
        return count;
    }
    public int getUserCountReview(String name)throws SQLException{
        int count = 0;
        Connection connection = TransactionManager.getConnection();
        PreparedStatement preparedStmt = null;
        preparedStmt = connection.prepareStatement(SQL_GET_USER_COUNT_TOTAL_REVIEW);
        preparedStmt.setString(1,name);
        ResultSet resultSet = preparedStmt.executeQuery();
        connection.commit();
        while (resultSet.next()){
            count = resultSet.getInt(DBConstants.PullRequest.PULL_REQUEST_COUNT_BY_STATE);
        }
        resultSet.close();
        preparedStmt.close();
        connection.close();
        return count;
    }
}