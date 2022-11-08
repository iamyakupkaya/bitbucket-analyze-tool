package com.orion.bitbucket.service;


import com.orion.bitbucket.entity.ITopEntity;
import com.orion.bitbucket.entity.pull_request.PREntity;
import org.json.JSONException;

public interface IPullRequestService {

    public boolean getPullRequestFromAPI(String url, PREntity entity) throws JSONException;
}
