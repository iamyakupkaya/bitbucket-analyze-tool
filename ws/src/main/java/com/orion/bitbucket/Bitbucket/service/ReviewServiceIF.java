package com.orion.bitbucket.Bitbucket.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;

import com.orion.bitbucket.Bitbucket.model.PullRequestDO;
import com.orion.bitbucket.Bitbucket.model.ReviewDO;

public interface ReviewServiceIF {

    int getReviewCount() throws SQLException;
    ArrayList<ReviewDO.PullRequestReviewRelation> getReviewsByUsername(String username) throws SQLException;
    int getReviewIdByUsername(String username) throws SQLException;

}
