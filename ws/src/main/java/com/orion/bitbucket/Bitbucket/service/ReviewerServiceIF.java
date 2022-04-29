package com.orion.bitbucket.Bitbucket.service;
import java.util.ArrayList;

import com.orion.bitbucket.Bitbucket.model.PullRequestDO;
import com.orion.bitbucket.Bitbucket.model.ReviewerDO;

public interface ReviewerServiceIF {
    

    public ArrayList<ReviewerDO> getMergedPRReviewList();
    public ArrayList<ReviewerDO> getOpenPRReviewList();
    public ArrayList<ReviewerDO> getDeclinedPRReviewList();


    public ArrayList<PullRequestDO> getMergedPRListReviewedByUsername(String username);
    public ArrayList<ReviewerDO> getOpenPRReviewListByUsername(String username);
    public ArrayList<ReviewerDO> getDeclinedPRReviewListByUsername(String username);

    public int getMergedPRCountReviewedByUsername(String username);
    public int getOpenPRReviewCountByUsername(String username);
    public int getDeclinedPRReviewCountByUsername(String username);


    public ArrayList<ReviewerDO> getReviewersByPRId(int id);


}
