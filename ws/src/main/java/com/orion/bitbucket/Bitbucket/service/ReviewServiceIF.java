package com.orion.bitbucket.Bitbucket.service;

import java.sql.SQLException;
import java.util.ArrayList;
import com.orion.bitbucket.Bitbucket.model.ReviewDO;

public interface ReviewServiceIF {

    int getTotalReviewCount() throws SQLException;
    ArrayList<ReviewDO.PullRequestReviewRelation> getReviewsByUsernameAndStatus(String username, String status, int pagination) throws SQLException;
    ArrayList<ReviewDO.PullRequestReviewRelation> getReviewsByUsername(String username) throws SQLException;
    int getReviewIdByUsername(String username) throws SQLException;
    ArrayList<ReviewDO.PullRequestReviewRelation> getReviewsWithPullRequestState(String state) throws SQLException;
    ArrayList<ReviewDO.PullRequestReviewRelation> mostReviewedPullRequest() throws SQLException;
    ArrayList<ReviewDO> getReviewsWithPullRequestIdAndStatus(int id, String status) throws SQLException;
    public int reviewer_id();
}
