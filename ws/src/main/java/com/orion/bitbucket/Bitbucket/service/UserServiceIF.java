package com.orion.bitbucket.Bitbucket.service;

import com.orion.bitbucket.Bitbucket.model.UserDO;

import java.sql.SQLException;
import java.util.ArrayList;

public interface UserServiceIF {
    public void getCollectUserInformation()throws SQLException;
    ArrayList<UserDO> getAllUsers() throws SQLException;
    ArrayList<UserDO> getAllUserWıthRole(String Role) throws SQLException;
}
