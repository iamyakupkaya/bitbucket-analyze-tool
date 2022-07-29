package com.orion.bitbucket.Bitbucket.service;

import com.orion.bitbucket.Bitbucket.model.AuthorDO;
import com.orion.bitbucket.Bitbucket.model.UserDO;
import java.sql.SQLException;
import java.util.ArrayList;

public interface TeamServiceIF {
    public ArrayList<UserDO> getTeamUsers(String TeamCode) throws SQLException;
    public void insertTeams(String TeamCode,String manager) throws SQLException;
    public void deleteTeam(String TeamCode) throws SQLException;
    public ArrayList<String> getAllTeams()throws SQLException;
    public ArrayList<AuthorDO> getTeamUsersStatistics(ArrayList<String> names) throws SQLException;
    public int getTeamsTotalPR() throws SQLException;
    public void updateTeamMembersWithUsername(String username, String teamCode) throws SQLException;
    public ArrayList<String> getAllUsername() throws SQLException;
    public String getTeamManager(String teamCode) throws SQLException;
    public void updateTeamManager(String manager, String teamCode) throws SQLException;
    public void getTeamManagerAssign(String teamCode,String username) throws SQLException;

}
