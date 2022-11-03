package com.orion.bitbucket.services;

import com.orion.bitbucket.entities.ITopEntity;

public interface ICommonService {
    boolean getDataFromAPI(String url, ITopEntity entity);
}
