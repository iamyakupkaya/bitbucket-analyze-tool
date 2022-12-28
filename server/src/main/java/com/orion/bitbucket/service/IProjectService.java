package com.orion.bitbucket.service;

import org.json.JSONException;

public interface IProjectService {
    public boolean getProjectsFromAPI(String url) throws JSONException;
}
