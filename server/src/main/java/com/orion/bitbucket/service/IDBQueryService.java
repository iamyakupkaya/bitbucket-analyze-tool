package com.orion.bitbucket.service;

public interface IDBQueryService {

    public abstract void findPRSByEmail (String email, String DBCollectionName);
}
