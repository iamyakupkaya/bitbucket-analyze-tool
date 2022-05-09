package com.orion.bitbucket.Bitbucket.service;

import com.orion.bitbucket.Bitbucket.model.PullRequestDO;

import java.util.ArrayList;

public interface PullRequestServiceIF {

    public int getMergedPRCount();

    public int getOpenPRCount();

    public int getDeclinedPRCount();

    public ArrayList<PullRequestDO> getMergedPRList();

    public ArrayList<PullRequestDO> getOpenPRList();

    public ArrayList<PullRequestDO> getDeclinedPRList();

    public ArrayList<PullRequestDO> getMergedPRListByUsername(String username);

    public ArrayList<PullRequestDO> getOpenPRListByUsername(String username);

    public ArrayList<PullRequestDO> getDeclinedPRListByUsername(String username);

    public int getMergedPRCountByUsername(String username);

    public int getOpenPRCountByUsername(String username);

    public int getDeclinedPRCountByUsername(String username);
}
