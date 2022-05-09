package com.orion.bitbucket.Bitbucket.service;

import java.util.ArrayList;

import com.orion.bitbucket.Bitbucket.model.PullRequestDO;
import com.orion.bitbucket.Bitbucket.model.ReviewerDO;

import org.springframework.stereotype.Service;

@Service
public class ReviewerService extends BaseService implements ReviewerServiceIF {



    public ArrayList<ReviewerDO> getMergedPRReviewList() {
        ArrayList<ReviewerDO> list = new ArrayList<ReviewerDO>();
        ArrayList<PullRequestDO> mergePR = this.mergedPRList;
        for (int i = 0; i < mergePR.size(); i++) {
            ArrayList<ReviewerDO> reviewers = mergePR.get(i).getReviewerList();
          for (int j = 0; j< reviewers.size(); j++) {
            list.add(new ReviewerDO(reviewers.get(j).getDisplayName(), reviewers.get(j).getEmailAddress(), reviewers.get(j).getStatus(), reviewers.get(j).getApproved()));
          }
        }
        System.out.println(list);
        return list;
    }


    public ArrayList<ReviewerDO> getOpenPRReviewList() {
        ArrayList<ReviewerDO> list = new ArrayList<ReviewerDO>();
        ArrayList<PullRequestDO> openPR = this.openPRList;
        for (int i = 0; i < openPR.size(); i++) {
            ArrayList<ReviewerDO> reviewers = openPR.get(i).getReviewerList();
          for (int j = 0; j< reviewers.size(); j++) {
            list.add(new ReviewerDO(reviewers.get(j).getDisplayName(), reviewers.get(j).getEmailAddress(), reviewers.get(j).getStatus(), reviewers.get(j).getApproved()));
          }
        }
        System.out.println(list);
        return list;
    }

    
    public ArrayList<ReviewerDO> getDeclinedPRReviewList() {
        ArrayList<ReviewerDO> list = new ArrayList<ReviewerDO>();
        ArrayList<PullRequestDO> declinedPR = this.declinedPRList;
        for (int i = 0; i < declinedPR.size(); i++) {
            ArrayList<ReviewerDO> reviewers = declinedPR.get(i).getReviewerList();
          for (int j = 0; j< reviewers.size(); j++) {
            list.add(new ReviewerDO(reviewers.get(j).getDisplayName(), reviewers.get(j).getEmailAddress(), reviewers.get(j).getStatus(), reviewers.get(j).getApproved()));
          }
        }
        System.out.println(list);
        return list;
    }
    // En fazla review yapan kisi
    
    public ArrayList<ReviewerDO> getAllReview() {
        ArrayList<ReviewerDO> allReviewer = new ArrayList<ReviewerDO>();
        allReviewer.addAll(getMergedPRReviewList());
        allReviewer.addAll(getOpenPRReviewList());
        allReviewer.addAll(getDeclinedPRReviewList());

        return allReviewer;
    }



    public String getTopReviewer(){


        return null;
    }

  
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



    public ArrayList<PullRequestDO> getOpenPRListReviewedByUsername(String username) {
        ArrayList<PullRequestDO> list = new ArrayList<PullRequestDO>();
        ArrayList<PullRequestDO> openPRList = this.openPRList;
        for (int i = 0; i < openPRList.size(); i++){
            ArrayList<ReviewerDO> reviewers = openPRList.get(i).getReviewerList();
            for (int j = 0; j < reviewers.size(); j++){
                if(reviewers.get(j).getDisplayName().equals(username)) {
                   list.add(openPRList.get(i));
               }
            }
        }
        return list;
    }


    public ArrayList<PullRequestDO> getDeclinedPRListReviewedByUsername(String username) {
        ArrayList<PullRequestDO> list = new ArrayList<PullRequestDO>();
        ArrayList<PullRequestDO> declinedPRList = this.declinedPRList;
        for (int i = 0; i < declinedPRList.size(); i++){
            ArrayList<ReviewerDO> reviewers = declinedPRList.get(i).getReviewerList();
            for (int j = 0; j < reviewers.size(); j++){
                if(reviewers.get(j).getDisplayName().equals(username)) {
                   list.add(declinedPRList.get(i));
               }
            }
        }
        return list;
    }


    // Returns a number that indicates a specific user has reviewed pull requests that are merged
    public int getMergedPRCountReviewedByUsername(String username) {
        return this.getMergedPRListReviewedByUsername(username).size();
    }

    
    public int getOpenPRCountReviewedByUsername(String username) {
        return this.getOpenPRListReviewedByUsername(username).size();
    }

    
    public int getDeclinedPRCountReviewedByUsername(String username) {
        return this.getDeclinedPRListReviewedByUsername(username).size();
    }


    
    public ArrayList<ReviewerDO> getReviewersByPRId(int id) {
        // TODO Auto-generated method stub
        return null;
    }
    

}
