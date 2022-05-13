package com.orion.bitbucket.Bitbucket.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.orion.bitbucket.Bitbucket.model.PullRequestDO;
import com.orion.bitbucket.Bitbucket.model.ReviewDO;

public interface ReviewServiceIF {


    public ArrayList<ReviewDO> getMergedPRReviewList();

    public ArrayList<ReviewDO> getOpenPRReviewList();

    public ArrayList<ReviewDO> getDeclinedPRReviewList();


    public ArrayList<PullRequestDO> getMergedPRListReviewedByUsername(String username);

    public ArrayList<PullRequestDO> getOpenPRListReviewedByUsername(String username);

    public ArrayList<PullRequestDO> getDeclinedPRListReviewedByUsername(String username);

    public int getMergedPRCountReviewedByUsername(String username);

    public int getOpenPRCountReviewedByUsername(String username);

    public int getDeclinedPRCountReviewedByUsername(String username);


    public ArrayList<ReviewDO> getReviewersByPRId(int id);

    public Map<String, Long> getTopReviewer();

    public ArrayList<ReviewDO> getAllReview();

    public ArrayList<String> getAllReviewer();

    public int getAllReviewerCount();
    
    public int getAllReviewCount();

}
