package com.orion.bitbucket.services;


public interface IAllPRSService {

    boolean getAllPRS();

    void findAllPRWithEmail(String email);
}
