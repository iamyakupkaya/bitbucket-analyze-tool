package com.orion.bitbucket.Bitbucket.service;

import java.util.ArrayList;

import com.orion.bitbucket.Bitbucket.model.AuthorDO;

public interface AuthorServiceIF {

    public int getAuthorCount();
    public ArrayList<AuthorDO> getCountOfPrStatesOfAllAuthor();
    public ArrayList<String> getAllAuthor();

}
