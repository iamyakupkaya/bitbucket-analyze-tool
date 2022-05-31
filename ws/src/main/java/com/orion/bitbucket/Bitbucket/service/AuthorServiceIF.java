package com.orion.bitbucket.Bitbucket.service;

import com.orion.bitbucket.Bitbucket.model.AuthorDO;

import java.sql.SQLException;
import java.util.ArrayList;

public interface AuthorServiceIF {

    int getAuthorCount() throws SQLException;
    ArrayList<String> getAllAuthor() throws SQLException;
    ArrayList<AuthorDO> getAllAuthors() throws SQLException;
    int getCountPRByStateAndUsername(String state, String username) throws SQLException;
    AuthorDO.TopAuthor getTopAuthor() throws SQLException;
    AuthorDO.TopAuthor getTopAuthorAtOpen() throws SQLException;
    AuthorDO.TopAuthor getTopAuthorAtMerged() throws SQLException;
    AuthorDO.TopAuthor getTopAuthorAtDeclined() throws SQLException;
    ArrayList<AuthorDO> getCountPRStatesByUsername(String name) throws SQLException;

    AuthorDO.TopAuthor getTopAuthorWithDateInterval(int day) throws SQLException;
    AuthorDO.TopAuthor getTopAuthorWithDateIntervalAndState(int day, String state) throws SQLException ;

}