package com.orion.bitbucket.Bitbucket.service;

import com.orion.bitbucket.Bitbucket.model.UserDO;

import java.sql.SQLException;
import java.util.ArrayList;

public interface UserServiceIF {
    void getCollectUserInformation()throws SQLException;
    ArrayList<UserDO> getAllUsers() throws SQLException;
    ArrayList<UserDO> getAllUserWithRole(String Role) throws SQLException;
    ArrayList<String> getUserFirstAndLastName(String username)throws SQLException;
    UserDO getUserInformation(String username)throws SQLException;
    void getDeleteUserWithUserName(String username)throws SQLException;
    void getUpdateUserWithUserName(String userName,String firstName,String lastName,
                       String password,String email, String teamCode, String role, String oldUsername) throws SQLException;
    int getUserCountTotalPR(String username)throws SQLException;
    int getUserCountReview(String name)throws SQLException;
    void insertUser(String userName,String firstName,String lastName,
                    String password,String email, String teamCode, String role) throws SQLException;
}
