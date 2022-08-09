package com.orion.bitbucket.Bitbucket.service;

import com.orion.bitbucket.Bitbucket.dbc.DBConstants;
import com.orion.bitbucket.Bitbucket.dbc.TransactionManager;
import com.orion.bitbucket.Bitbucket.log.Log;
import com.orion.bitbucket.Bitbucket.model.AuthorDO;
import com.orion.bitbucket.Bitbucket.model.UserDO;
import com.orion.bitbucket.Bitbucket.security.AdministratorServiceIF;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.sql.*;
import java.util.ArrayList;

@Service
public class TeamService extends BaseService implements TeamServiceIF{
    @Autowired
    AdministratorServiceIF administratorServiceIF;

    private final boolean IS_TEAMS_LOGGING = false;
    private int getTeamsTotalPR = 0;
    private final String SQL_GET_TEAMS_WITH_TEAM_CODE= "select * from users where team_code = ?";
    private final String SQL_INSERT_TEAM = "insert into teams(id,team_code,manager) values (?,?,?)";
    private final String SQL_DELETE_TEAMS = "delete from teams where team_code = ?";
    private final String SQL_UPDATE_MEMBERS_DEFAULT_TEAM_CODE = "update users set team_code = ?, role = ? where team_code = ?";
    private final String SQL_UPDATE_DEFAULT_AUTHORITY = "update users set team_code = ?, role = ? where user_name = ?";
    private final String SQL_GET_TEAMS_CODE = "select team_code from teams";
    private final String SQL_GET_ALL_AUTHORS = "select * from author";
    private final String SQL_UPDATE_TEAM_MEMBERS_WITH_USERNAME = "update users set team_code = ? where user_name = ?";
    private final String SQL_GET_TEAM_MAX_COUNTER_ID = "select max(id) from teams ";
    private final String SQL_GET_MANAGER = "select first_name, last_name from users where team_code = ? and role = ?";
    private final String SQL_UPDATE_TEAM_MANAGER ="update teams set manager = ? where team_code = ?";
    private final String SQL_SET_MANAGER_ASSIGN = "update users set role = ? where  team_code = ? AND user_name = ?";
    private final String SQL_GET_PERMISSION = "select user_name from users where team_code = ? and role = ?";
    private final String SQL_GET_LEADER_WITH_TEAM = "select user_name from users where team_code = ? and role = ?";
    private final String SQL_GET_USER_WITH_ROLE = "select user_name from users where role = ?";
    private final String SQL_SET_LEADER = "update users set role = ? where user_name = ?";

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
    public void insertTeams(String TeamCode) throws SQLException {
        Connection connection = null;
        PreparedStatement preparedStmt = null;
        try {
            connection = TransactionManager.getConnection();
            preparedStmt = connection.prepareStatement(SQL_INSERT_TEAM);
            preparedStmt.setInt(1, maxUserID());
            preparedStmt.setString(2, TeamCode);
            preparedStmt.setString(3,DBConstants.Teams.DEFAULT_MANAGER);
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
        // Group members should be set default
            setGroupMembersDefault(TeamCode);
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
    public void setGroupMembersDefault(String teamCode)throws SQLException{
        ArrayList<String> leaders = getTeamLeaders(teamCode);
        for(String leader:leaders){
            administratorServiceIF.deleteAuthority(leader);
        }
        Connection connection = null;
        PreparedStatement preparedStmt = null;
        try {
            connection = TransactionManager.getConnection();
            preparedStmt = connection.prepareStatement(SQL_UPDATE_MEMBERS_DEFAULT_TEAM_CODE);
            preparedStmt.setString(1, DBConstants.User.DEFAULT_USERS_TEAM_EMPTY);
            preparedStmt.setString(2, DBConstants.User.USER_ROLE_USER);
            preparedStmt.setString(3, teamCode);
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
    public void setDefaultAuthority(String username)throws SQLException{
        administratorServiceIF.deleteAuthority(username);
        Connection connection = null;
        PreparedStatement preparedStmt = null;
        try {
            connection = TransactionManager.getConnection();
            preparedStmt = connection.prepareStatement(SQL_UPDATE_DEFAULT_AUTHORITY);
            preparedStmt.setString(1, DBConstants.User.DEFAULT_USERS_TEAM_EMPTY);
            preparedStmt.setString(2, DBConstants.User.USER_ROLE_USER);
            preparedStmt.setString(3, username);
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
    public ArrayList<String> getTeamLeaders(String teamCode)throws SQLException{
        ArrayList<String> leaderList = null;
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            leaderList = new ArrayList<String>();
            connection = TransactionManager.getConnection();
            preparedStatement = connection.prepareStatement(SQL_GET_LEADER_WITH_TEAM);
            preparedStatement.setString(1,teamCode);
            preparedStatement.setString(2,DBConstants.User.USER_ROLE_LEADER);
            resultSet = preparedStatement.executeQuery();
            connection.commit();
            while (resultSet.next()){
                 leaderList.add(resultSet.getString(DBConstants.User.USER_NAME));
            }
        }catch (Exception exception){
            if (IS_TEAMS_LOGGING){Log.logger(Log.LogConstant.TAG_WARN,String.valueOf(exception));}
        }finally {
            resultSet.close();
            preparedStatement.close();
            connection.close();
        }
        return leaderList;
    }
    public void updateTeamManagerLEADER(String manager, String teamCode) throws SQLException{
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
    }  public void updateTeamManagerUSER(String teamCode) throws SQLException{
        ArrayList<String> leaders = getTeamLeaders(teamCode);
        String lastLeaderName = null;
        for(String leader:leaders){
            lastLeaderName = leader;
        }
        Connection connection = null;
        PreparedStatement preparedStmt = null;
        try {
            connection = TransactionManager.getConnection();
            preparedStmt = connection.prepareStatement(SQL_UPDATE_TEAM_MANAGER);
            if(lastLeaderName == null){lastLeaderName = DBConstants.Teams.DEFAULT_MANAGER;
                preparedStmt.setString(1, lastLeaderName);}
            else{preparedStmt.setString(1, lastLeaderName);}
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
        getTeamsTotalPR = 0;
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
    public String getTeamManagerTitle(String teamCode) throws SQLException{
        String manager =  null;
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
            try {
                connection = TransactionManager.getConnection();
                preparedStatement = connection.prepareStatement(SQL_GET_MANAGER);
                preparedStatement.setString(1, teamCode);
                preparedStatement.setString(2,DBConstants.User.USER_ROLE_LEADER);
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
    public void getTeamRoleAssign(String teamCode,String username, String role) throws SQLException{
            Connection connection = null;
            PreparedStatement preparedStatement = null;
            try {
                connection = TransactionManager.getConnection();
                preparedStatement = connection.prepareStatement(SQL_SET_MANAGER_ASSIGN);
                preparedStatement.setString(1, role);
                preparedStatement.setString(2, teamCode);
                preparedStatement.setString(3, username);
                preparedStatement.executeUpdate();
                connection.commit();
                if(role.equals(DBConstants.User.USER_ROLE_LEADER)){getPermissionUser(teamCode); updateTeamManagerLEADER(username,teamCode);}
                else{administratorServiceIF.deleteAuthority(username); updateTeamManagerUSER(teamCode);}
            }catch (Exception exception){
                if (IS_TEAMS_LOGGING){Log.logger(Log.LogConstant.TAG_WARN,String.valueOf(exception));}
            }finally {
                preparedStatement.close();
                connection.close();
            }
    }
    public void getPermissionUser(String teamCode) throws SQLException{
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = TransactionManager.getConnection();
            preparedStatement = connection.prepareStatement(SQL_GET_PERMISSION);
            preparedStatement.setString(1, teamCode);
            preparedStatement.setString(2, DBConstants.User.USER_ROLE_LEADER);
            resultSet = preparedStatement.executeQuery();
            connection.commit();
            while (resultSet.next()) {
                String username = resultSet.getString(DBConstants.User.USER_NAME);
                if(administratorServiceIF.checkAdmin(username)){setAuthorityLeader(username);}
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
    }
    public void setAuthorityLeader(String username) throws SQLException{
        String password = username;
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(password);
        administratorServiceIF.setAdmin(username,encodedPassword,DBConstants.User.USER_ROLE_LEADER);
    }
    public ArrayList<String> getAllUserWithRole(boolean assignLeader) throws SQLException{
        ArrayList<String> leaders = null;
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try{
            leaders = new ArrayList<>();
            leaders.clear();
            connection = TransactionManager.getConnection();
            preparedStatement = connection.prepareStatement(SQL_GET_USER_WITH_ROLE);
            if(assignLeader){preparedStatement.setString(1, DBConstants.User.USER_ROLE_LEADER);}
            else{preparedStatement.setString(1, DBConstants.User.USER_ROLE_USER);}
            resultSet = preparedStatement.executeQuery();
            connection.commit();
            while (resultSet.next()){
            leaders.add(resultSet.getString(DBConstants.User.USER_NAME));
            }
        }catch (Exception exception){
            if (IS_TEAMS_LOGGING){Log.logger(Log.LogConstant.TAG_WARN,String.valueOf(exception));}
        }finally {
            resultSet.close();
            preparedStatement.close();
            connection.close();
        }
        return leaders;
    }
    public void getUserRoleLeader(String username, boolean makeLeader) throws SQLException{
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = TransactionManager.getConnection();
            preparedStatement = connection.prepareStatement(SQL_SET_LEADER);
            if(makeLeader){preparedStatement.setString(1, DBConstants.User.USER_ROLE_LEADER);}
            else{preparedStatement.setString(1, DBConstants.User.USER_ROLE_USER);}
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
