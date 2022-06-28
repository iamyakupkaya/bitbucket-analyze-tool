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
    private final String SQL_DELETE_USER_WITH_USER_NAME =  "<-->";
    private final String SQL_UPDATE_USER = "<-->";
    private final String SQL_ALL_USERS = "select * from users";
    private final String SQL_GET_ALL_USERS_WITH_ROLE = "select * from users where role = ? ";

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
        insertUsers("syagsagan","Sinan","YAGSAGAN","123456",
                "sinan.yagsagan@orioninc.com","NRD1222",DBConstants.User.USER_ROLE_ADMIN);
        insertUsers("ogulusoy","Oguzhan","ULUSOY","123456",
                "oguzhan.ulusoy@orioninc.com","NRD1222",DBConstants.User.USER_ROLE_NORMAL);
        insertUsers("rsaglam","Ridvan","SAGLAM","123456",
                "ridvan.saglam@orioninc.com","NRD1222",DBConstants.User.USER_ROLE_LEADER);
    }

    public void insertUsers(String userName,String firstName,String lastName,
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
    public ArrayList<String> getUserWıthTeamCode(String teamCode)throws SQLException{
        ArrayList<String> teamList = new ArrayList<>();
        return teamList;
    }

    public void getUserUpdate(String name)throws SQLException{

    }
    public void getUserDelete(String name)throws SQLException{}
    public void getUserAdd(String name)throws SQLException{}

}