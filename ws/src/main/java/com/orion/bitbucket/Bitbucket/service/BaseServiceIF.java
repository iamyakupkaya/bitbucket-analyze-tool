package com.orion.bitbucket.Bitbucket.service;

import com.orion.bitbucket.Bitbucket.model.PullRequestDO;
import java.sql.SQLException;
import java.util.ArrayList;
public interface BaseServiceIF {
    public void getData();
    public void updatePullRequest();
    ArrayList<PullRequestDO> updateInformationDetails(ArrayList<Integer> updatePrList) throws SQLException;
    public ArrayList<Integer> updatePrList();
}
