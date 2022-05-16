package com.orion.bitbucket.Bitbucket.service;

import java.util.ArrayList;

import com.orion.bitbucket.Bitbucket.model.ReviewerDO;

public interface ReviewerServiceIF {
    
    public ArrayList<ReviewerDO> getCountOfReviewStatesOfAllReviewer();

    public ArrayList<ReviewerDO> getCountOfReviewStatesWithDisplayName(String displayName);

    public ArrayList<String> getAllReviewer();

}
