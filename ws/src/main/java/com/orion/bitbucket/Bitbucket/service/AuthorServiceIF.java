package com.orion.bitbucket.Bitbucket.service;

import java.util.ArrayList;
import java.util.Map;

import com.orion.bitbucket.Bitbucket.model.AuthorDO;

public interface AuthorServiceIF {

    public int getAuthorCount();

    public ArrayList<AuthorDO> getCountOfPrStatesOfAllAuthor();

    public ArrayList<AuthorDO> getCountOfPrStatesWithDisplayName(String displayName);

    public ArrayList<String> getAllAuthor();

    public Map<String, Long> getTopAuthor();

}
