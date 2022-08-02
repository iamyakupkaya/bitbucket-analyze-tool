package com.orion.bitbucket.Bitbucket.security;

import com.orion.bitbucket.Bitbucket.dbc.DBConstants;
import com.orion.bitbucket.Bitbucket.dbc.TransactionManager;
import com.orion.bitbucket.Bitbucket.log.Log;
import org.springframework.stereotype.Service;
import java.sql.*;

@Service
public class AdministratorService implements AdministratorServiceIF{
    private final boolean IS_ADMINISTRATOR_LOGGING = false;
    private static final String  DEFAULT_ADMIN_USERNAME= "admin";
    private static final String  DEFAULT_ADMIN_PASSWORD= "$2a$12$cIiRLizVVIMVg3Zr8XJwRuKGClDmeYrItX36WS6xIwdPJBoRiFr6u";
    private static final String  DEFAULT_ADMIN_ROLE= "ADMIN";
    private final String SQL_SET_ADMIN = "insert into administrator(id,username,password,role) values(?,?,?,?)";
    private final String SQL_GET_ADMINISTRATOR_MAX_COUNTER_ID = "select max(id) from administrator";
    private final String SQL_GET_CHECK_ADMINISTRATOR_USERNAME = "select count(*) from administrator where username = ?";

    public boolean checkAdmin() throws SQLException{
        int existUsername = 0;
        Connection connection = null;
        PreparedStatement preparedStmt = null;
        ResultSet resultSet = null;
        try {
            String checkUsername = null;
            connection = TransactionManager.getConnection();
            preparedStmt = connection.prepareStatement(SQL_GET_CHECK_ADMINISTRATOR_USERNAME);
            preparedStmt.setString(1, DEFAULT_ADMIN_USERNAME);
            resultSet = preparedStmt.executeQuery();
            connection.commit();
            while (resultSet.next()) {
                existUsername = resultSet.getInt(DBConstants.Administrator.CHECK_ADMINISTRATOR_USERNAME);
            }
        }catch (Exception exception){
            if (IS_ADMINISTRATOR_LOGGING) {
                Log.logger(Log.LogConstant.TAG_WARN, String.valueOf(exception));
            }
        }finally {
            resultSet.close();
            preparedStmt.close();
            connection.close();
        }
      return existUsername > 0 ? false : true;
    }
    public void setAdmin() throws SQLException {
        Connection connection = null;
        PreparedStatement preparedStmt = null;
        try {
            connection = TransactionManager.getConnection();
            preparedStmt = connection.prepareStatement(SQL_SET_ADMIN);
            preparedStmt.setLong(1, maxAdminID());
            preparedStmt.setString(2, DEFAULT_ADMIN_USERNAME);
            preparedStmt.setString(3, DEFAULT_ADMIN_PASSWORD);
            preparedStmt.setString(4, DEFAULT_ADMIN_ROLE);
            int row = preparedStmt.executeUpdate();
            connection.commit();
        } catch (Exception exception) {
            if (IS_ADMINISTRATOR_LOGGING) {
                Log.logger(Log.LogConstant.TAG_WARN, String.valueOf(exception));
            }
        } finally {
            preparedStmt.close();
            connection.close();
        }
    }
    public int maxAdminID() throws SQLException {
        int maxAdminID = 0;
        ResultSet resultSet = null;
        Statement statement = null;
        Connection connection = null;
        try {
            connection = TransactionManager.getConnection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery(SQL_GET_ADMINISTRATOR_MAX_COUNTER_ID);
            while (resultSet.next()) {
                int maxId = resultSet.getInt(DBConstants.User.USER_COUNTER_MAX_ID);
                maxAdminID = (maxId + 1);
            }
        } catch (Exception exception) {
            if (IS_ADMINISTRATOR_LOGGING) {
                Log.logger(Log.LogConstant.TAG_WARN, String.valueOf(exception));
            }
        } finally {
            resultSet.close();
            statement.close();
            connection.close();
        }
        return maxAdminID;
    }
}
