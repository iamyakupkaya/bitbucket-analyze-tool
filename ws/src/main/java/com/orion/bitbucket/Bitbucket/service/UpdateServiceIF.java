package com.orion.bitbucket.Bitbucket.service;

import com.orion.bitbucket.Bitbucket.model.PullRequestDO;
import java.sql.SQLException;
import java.util.ArrayList;
public interface UpdateServiceIF {
    void runUpdate();
    ArrayList<PullRequestDO> updateDetail() throws SQLException;
}
