package com.orion.bitbucket.service;

import com.orion.bitbucket.entity.project.ProjectEntity;
import org.json.JSONException;

public interface IProjectsService {
    public boolean getProjectsFromAPI(String url) throws JSONException;
}
