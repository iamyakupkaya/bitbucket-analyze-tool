package com.orion.bitbucket.Bitbucket.service;

import com.orion.bitbucket.Bitbucket.dbc.DBConstants;
import com.orion.bitbucket.Bitbucket.dbc.TransactionManager;
import com.orion.bitbucket.Bitbucket.model.UserDO;
import org.springframework.stereotype.Service;

import java.sql.*;
import java.util.ArrayList;

@Service
public class UserService extends BaseService implements UserServiceIF{
    private final boolean IS_USER_LOGGING = false;
    private static int COUNTER = 1000;
    private final String SQL_INSERT_USER = "insert into users(id,user_name,first_name,last_name,password,email_address,team_code,role) values (?,?,?,?,?,?,?,?)";
    private final String SQL_DELETE_USER_WITH_USER_NAME =  "delete from users where user_name = ?";
    private final String SQL_UPDATE_USER_WITH_USER_NAME = "update users set user_name = ?, first_name = ?,\n" +
            "\t\t\t\tlast_name = ?, password =?,\n" +
            "\t\t\t\temail_address = ?,team_code =?,\n" +
            "\t\t\t\trole = ? where user_name =?";
    private final String SQL_ALL_USERS = "select * from users";
    private final String SQL_GET_ALL_USERS_WITH_ROLE = "select * from users where role = ? ";
    private final String SQL_GET_FIRSTNAME_AND_LASTNAME_WITH_USERNAME = "select first_name, last_name from users where user_name = ?";
    private final String SQL_GET_USER_INFORMATION_WITH_USERNAME = "select * from users where user_name= ?";
    private final String SQL_GET_USER_COUNT_TOTAL_PULL_REQUEST = "select count(*) from pullrequest where slug = ?";
    private final String SQL_GET_USER_COUNT_TOTAL_REVIEW = "select count(*) from review where display_name = ?";

    public ArrayList<UserDO> getAllUsers() throws SQLException{
        ArrayList<UserDO> users = new ArrayList<UserDO>();
        Connection connection = TransactionManager.getConnection();
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(SQL_ALL_USERS);
        while (resultSet.next()){
            int id = resultSet.getInt(DBConstants.User.USER_ID);
            String user_name = resultSet.getString(DBConstants.User.USER_NAME);
            String first_name = resultSet.getString(DBConstants.User.USER_FIRST_NAME);
            String last_name = resultSet.getString(DBConstants.User.USER_LAST_NAME);
            String password = resultSet.getString(DBConstants.User.USER_PASSWORD);
            String email_address = resultSet.getString(DBConstants.User.USER_EMAIL_ADDRESS);
            String teamCode = resultSet.getString(DBConstants.User.USER_TEAM_CODE);
            String role = resultSet.getString(DBConstants.User.USER_ROLE);
            users.add(new UserDO(id,user_name,first_name,last_name,password,email_address,teamCode,role));
        }
        resultSet.close();
        statement.close();
        connection.close();
        return users;
    }
    public ArrayList<UserDO> getAllUserWıthRole(String Role) throws SQLException{
        ArrayList<UserDO> users = new ArrayList<UserDO>();
        Connection connection = TransactionManager.getConnection();
        PreparedStatement preparedStmt = null;
        preparedStmt = connection.prepareStatement(SQL_GET_ALL_USERS_WITH_ROLE);
        preparedStmt.setString(1,Role);
        ResultSet resultSet = preparedStmt.executeQuery();
        connection.commit();
        while (resultSet.next()){
            int id = resultSet.getInt(DBConstants.User.USER_ID);
            String user_name = resultSet.getString(DBConstants.User.USER_NAME);
            String first_name = resultSet.getString(DBConstants.User.USER_FIRST_NAME);
            String last_name = resultSet.getString(DBConstants.User.USER_LAST_NAME);
            String password = resultSet.getString(DBConstants.User.USER_PASSWORD);
            String email_address = resultSet.getString(DBConstants.User.USER_EMAIL_ADDRESS);
            String teamCode = resultSet.getString(DBConstants.User.USER_TEAM_CODE);
            String role = resultSet.getString(DBConstants.User.USER_ROLE);
            users.add(new UserDO(id,user_name,first_name,last_name,password,email_address,teamCode,role));
        }
        resultSet.close();
        preparedStmt.close();
        connection.close();
        return users;
    }

    public void getCollectUserInformation()throws SQLException{
        // Test Data
        insertUser("syagsagan","Sinan","YAGSAGAN","123456",
                "sinan.yagsagan@orioninc.com","NRD1222",DBConstants.User.USER_ROLE_ADMIN);
        insertUser("ogulusoy","Oguzhan","ULUSOY","123456",
                "oguzhan.ulusoy@orioninc.com","NRD1222",DBConstants.User.USER_ROLE_NORMAL);
        insertUser("rsaglam","Ridvan","SAGLAM","123456",
                "ridvan.saglam@orioninc.com","NRD1222",DBConstants.User.USER_ROLE_LEADER);
    }

    public void insertUser(String userName,String firstName,String lastName,
                               String password,String email, String teamCode, String role) throws SQLException{

        Connection connection = TransactionManager.getConnection();
        try {
            PreparedStatement preparedStmt = null;
            preparedStmt = connection.prepareStatement(SQL_INSERT_USER);
            preparedStmt.setInt(1,COUNTER);
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

            String first_name = resultSet.getString(DBConstants.User.USER_FIRST_NAME);
            String last_name = resultSet.getString(DBConstants.User.USER_LAST_NAME);
            list.add(first_name);
            list.add(last_name);
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
            String user_name = resultSet.getString(DBConstants.User.USER_NAME);
            String first_name = resultSet.getString(DBConstants.User.USER_FIRST_NAME);
            String last_name = resultSet.getString(DBConstants.User.USER_LAST_NAME);
            String password = resultSet.getString(DBConstants.User.USER_PASSWORD);
            String email_address = resultSet.getString(DBConstants.User.USER_EMAIL_ADDRESS);
            String teamCode = resultSet.getString(DBConstants.User.USER_TEAM_CODE);
            String role = resultSet.getString(DBConstants.User.USER_ROLE);
            user = new UserDO(id,user_name,first_name,last_name,password,email_address,teamCode,role);
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
    public void getUpdateUserWithUserName(String userName,String firstName,String lastName,
                              String password,String email, String teamCode, String role, String oldUsername) throws SQLException{

        Connection connection = TransactionManager.getConnection();
        PreparedStatement preparedStmt = null;
        preparedStmt = connection.prepareStatement(SQL_UPDATE_USER_WITH_USER_NAME);
        preparedStmt.setString(1,userName);
        preparedStmt.setString(2,firstName);
        preparedStmt.setString(3,lastName);
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