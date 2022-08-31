package com.orion.bitbucket.Bitbucket.service;

import com.orion.bitbucket.Bitbucket.model.AuthorDO;
import com.orion.bitbucket.Bitbucket.model.ReviewerDO;
import com.orion.bitbucket.Bitbucket.model.UserDO;
import java.sql.SQLException;
import java.util.ArrayList;

public interface TeamServiceIF {
    public ArrayList<UserDO> getTeamUsers(String TeamCode) throws SQLException;
    public void insertTeams(String TeamCode) throws SQLException;
    public void deleteTeam(String TeamCode) throws SQLException;
    public ArrayList<String> getAllTeams()throws SQLException;
    public ArrayList<AuthorDO> getTeamUsersStatistics(ArrayList<String> names) throws SQLException;
    public int getTeamsTotalPR() throws SQLException;
    public int getTeamsTotalReview();
    public void updateTeamMembersWithUsername(String username, String teamCode) throws SQLException;
    public String getTeamManagerTitle(String teamCode) throws SQLException;
    public void getTeamRoleAssign(String teamCode,String username,String role) throws SQLException;
    public void setDefaultAuthority(String username)throws SQLException;
    public void setAuthorityLeader(String username) throws SQLException;
    public ArrayList<String> getAllUserWithRole(boolean assignLeader) throws SQLException;
    public void getUserRoleLeader(String username, boolean makeLeader) throws SQLException;
    public ArrayList<ReviewerDO> getTeamReviewerStatistics(ArrayList<String> names) throws SQLException;

}
