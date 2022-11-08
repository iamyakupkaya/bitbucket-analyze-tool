package com.orion.bitbucket.service;

import com.orion.bitbucket.entity.ITopEntity;

public interface ICommonService {
    boolean getDataFromAPI(String url, ITopEntity entity);
}
