package com.orion.bitbucket.service;


import com.google.gson.JsonObject;
import com.mongodb.BasicDBObject;
import com.orion.bitbucket.entity.pull_request.PREntity;
import org.json.JSONObject;

import java.util.List;

public interface IQueryService {

    public abstract List<PREntity> getAllPullRequests (String query, String condition, String [] collectionNames);
    public abstract BasicDBObject getQuery(String query, String condition);

    String updateTeamNames(String[] userID, String teamNameText, String[] collectionNames);
}
