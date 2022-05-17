package com.orion.bitbucket.Bitbucket.service;

import com.orion.bitbucket.Bitbucket.model.PullRequestDO;

import java.sql.SQLException;
import java.util.ArrayList;

public interface PullRequestServiceIF {

    public int getAllPRCount() throws SQLException;

    public int getMergedPRCount() throws SQLException;

    public int getOpenPRCount() throws SQLException;

    public int getDeclinedPRCount() throws SQLException;

    public ArrayList<PullRequestDO> getMergedPRList() throws SQLException;

    public ArrayList<PullRequestDO> getOpenPRList() throws SQLException;

    public ArrayList<PullRequestDO> getDeclinedPRList() throws SQLException;

    public ArrayList<PullRequestDO> getMergedPRListByUsername(String username) throws SQLException;

    public ArrayList<PullRequestDO> getOpenPRListByUsername(String username) throws SQLException;

    public ArrayList<PullRequestDO> getDeclinedPRListByUsername(String username) throws SQLException;

    public int getMergedPRCountByUsername(String username) throws SQLException;

    public int getOpenPRCountByUsername(String username) throws SQLException;

    public int getDeclinedPRCountByUsername(String username) throws SQLException;
}
