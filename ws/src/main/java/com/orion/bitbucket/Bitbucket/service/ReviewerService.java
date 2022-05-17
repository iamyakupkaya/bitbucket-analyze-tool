package com.orion.bitbucket.Bitbucket.service;

import java.util.ArrayList;

import com.orion.bitbucket.Bitbucket.model.ReviewerDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReviewerService extends BaseService implements ReviewerServiceIF{

    @Autowired
    private ReviewServiceIF reviewServiceIF;

    @Override
    public ArrayList<ReviewerDO> getCountOfReviewStatesOfAllReviewer() {
        ArrayList<String> reviewerList = getAllReviewer();
        ArrayList<ReviewerDO> reviewerDOList = new ArrayList<ReviewerDO>();
        int total;
        int mergeReviewSize = 0;
        int openReviewSize = 0;
        int declinedReviewSize = 0;
        int approve = 0;
        int unApprove = 0;
        for (int i = 0; i < reviewerList.size(); i++) {
            mergeReviewSize = reviewServiceIF.getMergedPRCountReviewedByUsername(reviewerList.get(i));
            openReviewSize = reviewServiceIF.getOpenPRCountReviewedByUsername(reviewerList.get(i));
            declinedReviewSize = reviewServiceIF.getDeclinedPRCountReviewedByUsername(reviewerList.get(i));
            total = mergeReviewSize + openReviewSize + declinedReviewSize;
            System.out.println(reviewerList.get(i));
            unApprove = reviewServiceIF.getReviewerUnApproveCount(reviewerList.get(i));
            System.out.println(unApprove);
            approve = reviewServiceIF.getReviewerApproveCount(reviewerList.get(i));
            System.out.println(approve);
            reviewerDOList.add(new ReviewerDO(0,reviewerList.get(i), total,approve,unApprove)); // id must be added
        }
        return reviewerDOList;
    }

    @Override
    public ArrayList<ReviewerDO> getCountOfReviewStatesWithDisplayName(String displayName) {
        
        return null;
    }

    @Override
    public ArrayList<String> getAllReviewer() {
        ArrayList<String> reviewerList = reviewServiceIF.getAllReviewer();
        ArrayList<String> allReviewerList = new ArrayList<String>();
        for (int i = 0; i < reviewerList.size(); i++) {
            if (!allReviewerList.contains(reviewerList.get(i))) {
                allReviewerList.add(reviewerList.get(i));
            }
        }
        return reviewerList;
    }
    
}
