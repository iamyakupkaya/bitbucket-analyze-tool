package com.orion.bitbucket.Bitbucket.service;

import java.util.ArrayList;

import com.orion.bitbucket.Bitbucket.model.AuthorDO;

public interface ReviewerServiceIF {
    

    public ArrayList<AuthorDO> getMergedPRReviewList();
    public ArrayList<AuthorDO> getOpenPRReviewList();
    public ArrayList<AuthorDO> getDeclinedPRReviewList();


    public ArrayList<AuthorDO> getMergedPRReviewListByUsername(String username);
    public ArrayList<AuthorDO> getOpenPRReviewListByUsername(String username);
    public ArrayList<AuthorDO> getDeclinedPRReviewListByUsername(String username);

    public int getMergedPRReviewCountByUsername(String username);
    public int getOpenPRReviewCountByUsername(String username);
    public int getDeclinedPRReviewCountByUsername(String username);


    public ArrayList<AuthorDO> getReviewersByPRId(int id);


}
