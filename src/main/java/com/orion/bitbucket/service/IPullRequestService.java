package com.orion.bitbucket.service;


public interface IPullRequestService {

    boolean getAllPRS();

    void findAllPRWithEmail(String email);
}
