package com.orion.bitbucket.Bitbucket.service;

import com.orion.bitbucket.Bitbucket.model.UserDO;

import java.sql.SQLException;
import java.util.*;

public interface UserServiceIF {
    void getCollectUserInformation(String userName,String firstName,String lastName,
                                   String password,String email, String teamCode, String role)throws SQLException;
    ArrayList<UserDO> getAllUsers() throws SQLException;
    ArrayList<UserDO> getAllUserWithRole(String Role) throws SQLException;
    ArrayList<String> getUserFirstAndLastName(String username)throws SQLException;
    UserDO getUserInformation(String username)throws SQLException;
    void getDeleteUserWithUserName(String username)throws SQLException;
    int getUserCountTotalPR(String username)throws SQLException;
    int getUserCountReview(String name)throws SQLException;
    void insertUser(int id,String userName,String firstName,String lastName,
                    String password,String email, String teamCode, String role) throws SQLException;
    public void getUpdateUserInformation(String firstname,String lastname, String email, String username) throws SQLException;
    ArrayList<UserDO> getAllUserWithRoleAndTeam(String Role,String TeamCode) throws SQLException;
    ArrayList<UserDO> getAllUserWithTeam(String TeamCode) throws SQLException;
    public void insertUserTable() throws SQLException;
    public List<String> getRoles() throws SQLException;
}
