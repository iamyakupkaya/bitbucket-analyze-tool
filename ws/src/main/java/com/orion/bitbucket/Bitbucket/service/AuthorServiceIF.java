package com.orion.bitbucket.Bitbucket.service;

import com.orion.bitbucket.Bitbucket.model.AuthorDO;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;

public interface AuthorServiceIF {

    int getAuthorCount() throws SQLException;
    ArrayList<String> getAllAuthor() throws SQLException;
    int getCountPRByStateAndUsername(String state, String username) throws SQLException;
    AuthorDO.TopAuthor getTopAuthor() throws SQLException;
    AuthorDO.TopAuthor getTopAuthorAtOpen() throws SQLException;
    AuthorDO.TopAuthor getTopAuthorAtMerged() throws SQLException;
    AuthorDO.TopAuthor getTopAuthorAtDeclined() throws SQLException;
    ArrayList<AuthorDO> getAllAuthors() throws SQLException;

}