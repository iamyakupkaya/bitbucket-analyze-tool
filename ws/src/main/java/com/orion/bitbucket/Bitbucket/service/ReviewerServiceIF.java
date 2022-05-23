package com.orion.bitbucket.Bitbucket.service;

import java.sql.SQLException;
import java.util.ArrayList;
import com.orion.bitbucket.Bitbucket.model.ReviewerDO;

public interface ReviewerServiceIF {

    int getAllReviewerCount() throws SQLException;
    ArrayList<String> getAllReviewer() throws SQLException;
    int getCountReviewByStatusAndUsername(String state, String username) throws SQLException;
    ReviewerDO.TopReviewer getTopReviewer() throws SQLException;
    ArrayList<ReviewerDO> getAllReviewers() throws SQLException;
    ArrayList<ReviewerDO> getCountOfReviewStatesWithDisplayName(String name) throws SQLException;

    
}