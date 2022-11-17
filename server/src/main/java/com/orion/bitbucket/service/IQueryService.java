package com.orion.bitbucket.service;


import com.google.gson.JsonObject;
import com.orion.bitbucket.entity.pull_request.PREntity;
import org.json.JSONObject;

import java.util.List;

public interface IQueryService {

    public abstract List<PREntity> findPRSByEmail (String email, String DBCollectionName);
}
