package com.orion.bitbucket.Bitbucket.service;

import com.orion.bitbucket.Bitbucket.dbc.DBConstants;
import com.orion.bitbucket.Bitbucket.dbc.TransactionManager;
import com.orion.bitbucket.Bitbucket.log.Log;
import com.orion.bitbucket.Bitbucket.model.AuthorDO;
import com.orion.bitbucket.Bitbucket.model.UserDO;
import org.springframework.stereotype.Service;

import java.sql.*;
import java.util.ArrayList;

@Service
public class TeamService extends BaseService implements TeamServiceIF{
    private final boolean IS_TEAMS_LOGGING = false;
    private int getTeamsTotalPR = 0;
    private final String SQL_GET_TEAMS_WITH_TEAM_CODE= "select * from users where team_code = ?";
    private final String SQL_INSERT_TEAMS = "insert into teams(id,team_code,manager) values (?,?,?)";
    private final String SQL_DELETE_TEAMS = "delete from teams where team_code = ?";
    private final String SQL_GET_TEAMS_CODE = "select team_code from teams";
    private final String SQL_GET_ALL_AUTHORS = "select * from author";
    private final String SQL_UPDATE_TEAM_MEMBERS_WITH_USERNAME = "update users set team_code = ? where user_name = ?";
    private final String SQL_GET_TEAM_MAX_COUNTER_ID = "select max(id) from teams ";
    private final String SQL_GET_ALL_USERNAME = "select user_name from users";
    private final String SQL_GET_MANAGER = "select first_name, last_name from users where team_code = ? and role = 'leader'";
    private final String SQL_GET_OLD_MANAGER_USERNAME = "select user_name from users where team_code = ? and role = 'leader'";
    private final String SQL_UPDATE_TEAM_MANAGER ="update teams set manager = ? where team_code = ?";
    private final String SQL_SET_MANAGER_ASSIGN = "update users set role = 'leader' where  team_code = ? AND user_name = ?";
    private final String SQL_SET_OLD_MANAGER_ASSIGN = "update users set role = 'normal' where  team_code = ? AND user_name = ?";

    public void insertTeams(String TeamCode, String manager) throws SQLException {
        Connection connection = null;
        PreparedStatement preparedStmt = null;
        try {
            connection = TransactionManager.getConnection();
            preparedStmt = connection.prepareStatement(SQL_INSERT_TEAMS);
            preparedStmt.setInt(1, maxUserID());
            preparedStmt.setString(2, TeamCode);
            preparedStmt.setString(3,manager);
            int row = preparedStmt.executeUpdate();
            connection.commit();
        } catch (Exception exception) {
            if (IS_TEAMS_LOGGING) {
                Log.logger(Log.LogConstant.TAG_WARN, String.valueOf(exception));
            }
        } finally {
            preparedStmt.close();
            connection.close();
        }
    }
    public void deleteTeam(String TeamCode) throws SQLException{
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = TransactionManager.getConnection();
            preparedStatement = connection.prepareStatement(SQL_DELETE_TEAMS);
            preparedStatement.setString(1, TeamCode);
            preparedStatement.executeUpdate();
            connection.commit();
        }catch (Exception exception){
            if (IS_TEAMS_LOGGING){Log.logger(Log.LogConstant.TAG_WARN,String.valueOf(exception));}
        }finally {
            preparedStatement.close();
            connection.close();
        }
    }
    public void updateTeamManager(String manager, String teamCode) throws SQLException{
        Connection connection = null;
        PreparedStatement preparedStmt = null;
        try {
            connection = TransactionManager.getConnection();
            preparedStmt = connection.prepareStatement(SQL_UPDATE_TEAM_MANAGER);
            preparedStmt.setString(1, manager);
            preparedStmt.setString(2, teamCode);
            preparedStmt.executeUpdate();
            connection.commit();
        } catch (Exception exception) {
            if (IS_TEAMS_LOGGING) {
                Log.logger(Log.LogConstant.TAG_WARN, String.valueOf(exception));
            }
        } finally {
            preparedStmt.close();
            connection.close();
        }
    }
    public ArrayList<String> getAllTeams()throws SQLException{
        ArrayList<String> teams = null;
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try{
            teams = new ArrayList<String>();
            connection = TransactionManager.getConnection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery(SQL_GET_TEAMS_CODE);
            while (resultSet.next()){
                String teamCode = resultSet.getString(DBConstants.Teams.TEAM_CODE);
                teams.add(teamCode);
            }
        }catch (Exception exception){
            if (IS_TEAMS_LOGGING){Log.logger(Log.LogConstant.TAG_WARN,String.valueOf(exception));}
        }finally {
            resultSet.close();
            statement.close();
            connection.close();
        }
       return teams;
    }

    public ArrayList<UserDO> getTeamUsers(String TeamCode) throws SQLException {
        ArrayList<UserDO> teamUsers = null;
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try{
            teamUsers = new ArrayList<UserDO>();
            connection = TransactionManager.getConnection();
            preparedStatement = connection.prepareStatement(SQL_GET_TEAMS_WITH_TEAM_CODE);
            preparedStatement.setString(1, TeamCode);
            resultSet = preparedStatement.executeQuery();
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
                teamUsers.add(new UserDO(id,username,firstname,lastname,password,email,teamCode,role));
            }
        }catch (Exception exception){
            if(IS_TEAMS_LOGGING){
                Log.logger(Log.LogConstant.TAG_WARN,String.valueOf(exception));
            }
        }finally {
            resultSet.close();
            preparedStatement.close();
            connection.close();
        }
        return teamUsers;
    }
    public ArrayList<AuthorDO> getTeamUsersStatistics(ArrayList<String> names) throws SQLException {
        ArrayList<AuthorDO> authors = new ArrayList<AuthorDO>();
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        ArrayList<Integer> totalPR = null;
        totalPR = new ArrayList<>();
        for(String Name:names) {
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
                    if (Name.equals(name)){
                        authors.add(new AuthorDO(id, name, total, merge, open, declined));
                        totalPR.add(total);
                        getTeamsTotalPR = totalPR.stream().mapToInt(Integer::intValue).sum();
                    }
                }
            } catch (Exception exception) {
                if (IS_TEAMS_LOGGING) {
                    Log.logger(Log.LogConstant.TAG_WARN, String.valueOf(exception));
                }
            } finally {
                resultSet.close();
                statement.close();
                connection.close();
            }
        }
        totalPR.clear();
        return authors;
    }
    public int getTeamsTotalPR() throws SQLException{
        int totalTeamPr = getTeamsTotalPR;
        return totalTeamPr;
    }
    public void updateTeamMembersWithUsername(String username, String teamCode) throws SQLException{
        Connection connection = null;
        PreparedStatement preparedStmt = null;
        try {
            connection = TransactionManager.getConnection();
            preparedStmt = connection.prepareStatement(SQL_UPDATE_TEAM_MEMBERS_WITH_USERNAME);
            preparedStmt.setString(1, teamCode);
            preparedStmt.setString(2, username);
            preparedStmt.executeUpdate();
            connection.commit();
        } catch (Exception exception) {
            if (IS_TEAMS_LOGGING) {
                Log.logger(Log.LogConstant.TAG_WARN, String.valueOf(exception));
            }
        } finally {
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
            resultSet = statement.executeQuery(SQL_GET_TEAM_MAX_COUNTER_ID);
            while (resultSet.next()) {
                int maxId = resultSet.getInt(DBConstants.Teams.TEAMS_COUNTER_MAX_ID);
                maxUserId = (maxId + 1);
            }
        } catch (Exception exception) {
            if (IS_TEAMS_LOGGING) {
                Log.logger(Log.LogConstant.TAG_WARN, String.valueOf(exception));
            }
        } finally {
            resultSet.close();
            statement.close();
            connection.close();
        }
        return maxUserId;
    }
    public ArrayList<String> getAllUsername() throws SQLException{
        ArrayList<String> managers = null;
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try{
            managers = new ArrayList<String>();
            connection = TransactionManager.getConnection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery(SQL_GET_ALL_USERNAME);
            while (resultSet.next()){
                String username = resultSet.getString(DBConstants.User.USER_NAME);
                managers.add(username);
            }
        }catch (Exception exception){
            if (IS_TEAMS_LOGGING){Log.logger(Log.LogConstant.TAG_WARN,String.valueOf(exception));}
        }finally {
            resultSet.close();
            statement.close();
            connection.close();
        }
        return managers;
    }
    public String getTeamManager(String teamCode) throws SQLException{
        String manager =  null;
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
            try {
                connection = TransactionManager.getConnection();
                preparedStatement = connection.prepareStatement(SQL_GET_MANAGER);
                preparedStatement.setString(1, teamCode);
                resultSet = preparedStatement.executeQuery();
                connection.commit();
                while (resultSet.next()) {
                    String firstName = resultSet.getString(DBConstants.User.USER_FIRST_NAME);
                    String lastName = resultSet.getString(DBConstants.User.USER_LAST_NAME);
                    manager = lastName +" "+ firstName;
                }
            } catch (Exception exception) {
                if (IS_TEAMS_LOGGING) {
                    Log.logger(Log.LogConstant.TAG_WARN, String.valueOf(exception));
                }
            } finally {
                resultSet.close();
                preparedStatement.close();
                connection.close();
            }
        return manager;
    }
    public void getTeamManagerAssign(String teamCode,String username) throws SQLException{
        String oldTeamManagerUsername = getTeamOldManagerUsername(teamCode);
        setTeamOldManagerAssign(teamCode,oldTeamManagerUsername);

        Connection connection = null;
        PreparedStatement preparedStatement = null;
            try {
                connection = TransactionManager.getConnection();
                preparedStatement = connection.prepareStatement(SQL_SET_MANAGER_ASSIGN);
                preparedStatement.setString(1, teamCode);
                preparedStatement.setString(2, username);
                preparedStatement.executeUpdate();
                connection.commit();
            }catch (Exception exception){
                if (IS_TEAMS_LOGGING){Log.logger(Log.LogConstant.TAG_WARN,String.valueOf(exception));}
            }finally {
                preparedStatement.close();
                connection.close();
            }
    }
    public String getTeamOldManagerUsername(String teamCode) throws SQLException{
        String oldManagerUsername =  null;
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = TransactionManager.getConnection();
            preparedStatement = connection.prepareStatement(SQL_GET_OLD_MANAGER_USERNAME);
            preparedStatement.setString(1, teamCode);
            resultSet = preparedStatement.executeQuery();
            connection.commit();
            while (resultSet.next()) {
                oldManagerUsername = resultSet.getString(DBConstants.User.USER_NAME);
            }
        } catch (Exception exception) {
            if (IS_TEAMS_LOGGING) {
                Log.logger(Log.LogConstant.TAG_WARN, String.valueOf(exception));
            }
        } finally {
            resultSet.close();
            preparedStatement.close();
            connection.close();
        }
        return oldManagerUsername;
    }
    public void setTeamOldManagerAssign(String teamCode,String username) throws SQLException{
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = TransactionManager.getConnection();
            preparedStatement = connection.prepareStatement(SQL_SET_OLD_MANAGER_ASSIGN);
            preparedStatement.setString(1, teamCode);
            preparedStatement.setString(2, username);
            preparedStatement.executeUpdate();
            connection.commit();
        }catch (Exception exception){
            if (IS_TEAMS_LOGGING){Log.logger(Log.LogConstant.TAG_WARN,String.valueOf(exception));}
        }finally {
            preparedStatement.close();
            connection.close();
        }
    }
}
