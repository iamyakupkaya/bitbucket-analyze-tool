package com.orion.bitbucket.Bitbucket.service;

import java.sql.SQLException;
import java.util.ArrayList;
import com.orion.bitbucket.Bitbucket.model.ReviewDO;

public interface ReviewServiceIF {

    int getReviewCount() throws SQLException;
    ArrayList<ReviewDO.PullRequestReviewRelation> getReviewsByUsername(String username) throws SQLException;
    int getReviewIdByUsername(String username) throws SQLException;
    ArrayList<ReviewDO.PullRequestReviewRelation> getReviewsWithPullRequestState(String state) throws SQLException;

}
