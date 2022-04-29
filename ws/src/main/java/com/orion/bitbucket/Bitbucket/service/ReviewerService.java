package com.orion.bitbucket.Bitbucket.service;

import java.util.ArrayList;

import com.orion.bitbucket.Bitbucket.model.PullRequestDO;
import com.orion.bitbucket.Bitbucket.model.ReviewerDO;

public class ReviewerService extends BaseService implements ReviewerServiceIF {


    public ArrayList<ReviewerDO> getMergedPRReviewList() {
        // TODO Auto-generated method stub
        return null;
    }


    public ArrayList<ReviewerDO> getOpenPRReviewList() {
        // TODO Auto-generated method stub
        return null;
    }


    public ArrayList<ReviewerDO> getDeclinedPRReviewList() {
        // TODO Auto-generated method stub
        return null;
    }

    // En fazla review yapan kisi
    // En fazla review edilen pull request
    // En fazla review edilen 5 pull request (gereksiz olabilir bu)
    // En az review yapan kisi

    // Returns a list that is consists of pull request reviewed by username
    public ArrayList<PullRequestDO> getMergedPRListReviewedByUsername(String username) {
        ArrayList<PullRequestDO> list = new ArrayList<PullRequestDO>();
        ArrayList<PullRequestDO> mergedPRList = this.mergedPRList;
        for (int i = 0; i < mergedPRList.size(); i++) {
            ArrayList<ReviewerDO> reviewers = mergedPRList.get(i).getReviewerList();
            for (int j = 0; j < reviewers.size(); j++) {
                if (reviewers.get(j).getDisplayName().equals(username)) {
                    list.add(mergedPRList.get(i));
                }
            }
        }
        return list;
    }


    // TODO Rename and implement
    public ArrayList<ReviewerDO> getOpenPRReviewListByUsername(String username) {
        // TODO Auto-generated method stub
        return null;
    }

    // TODO Rename and implement
    public ArrayList<ReviewerDO> getDeclinedPRReviewListByUsername(String username) {
        // TODO Auto-generated method stub
        return null;
    }


    // Returns a number that indicates a specific user has reviewed pull requests that are merged
    public int getMergedPRCountReviewedByUsername(String username) {
        return this.getMergedPRListReviewedByUsername(username).size();
    }

    // TODO Rename and implement
    public int getOpenPRReviewCountByUsername(String username) {
        // TODO Auto-generated method stub
        return 0;
    }

    // TODO Rename and implement
    public int getDeclinedPRReviewCountByUsername(String username) {
        // TODO Auto-generated method stub
        return 0;
    }


    public ArrayList<ReviewerDO> getReviewersByPRId(int id) {
        // TODO Auto-generated method stub
        return null;
    }


}
