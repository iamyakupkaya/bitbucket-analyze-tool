package com.orion.bitbucket.Bitbucket.service;

import java.util.ArrayList;

import com.orion.bitbucket.Bitbucket.model.PullRequestDO;
import com.orion.bitbucket.Bitbucket.model.ReviewerDO;

public interface ReviewerServiceIF {


    public ArrayList<ReviewerDO> getMergedPRReviewList();

    public ArrayList<ReviewerDO> getOpenPRReviewList();

    public ArrayList<ReviewerDO> getDeclinedPRReviewList();


    public ArrayList<PullRequestDO> getMergedPRListReviewedByUsername(String username);

    public ArrayList<PullRequestDO> getOpenPRListReviewedByUsername(String username);

    public ArrayList<PullRequestDO> getDeclinedPRListReviewedByUsername(String username);

    public int getMergedPRCountReviewedByUsername(String username);

    public int getOpenPRCountReviewedByUsername(String username);

    public int getDeclinedPRCountReviewedByUsername(String username);


    public ArrayList<ReviewerDO> getReviewersByPRId(int id);


}
