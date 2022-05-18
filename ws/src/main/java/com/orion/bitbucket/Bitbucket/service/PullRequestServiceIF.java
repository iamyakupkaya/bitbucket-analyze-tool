package com.orion.bitbucket.Bitbucket.service;

import com.orion.bitbucket.Bitbucket.model.PullRequestDO;

import java.sql.SQLException;
import java.util.ArrayList;

public interface PullRequestServiceIF {

    int getAllPRCount() throws SQLException;
    int getPRCountByState(String status) throws SQLException;
    ArrayList<PullRequestDO> getPRListByState(String status) throws SQLException;
    ArrayList<PullRequestDO> getPRListByStateAndUsername(String status, String username) throws SQLException;
    int getPRCountByStateAndUsername(String state, String username) throws SQLException;
    PullRequestDO getPullRequestById(int pullRequestId) throws SQLException;
}
