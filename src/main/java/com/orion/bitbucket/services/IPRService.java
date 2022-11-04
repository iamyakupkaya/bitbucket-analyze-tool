package com.orion.bitbucket.services;


public interface IPRService {

    boolean getAllPRS();

    void findAllPRWithEmail(String email);
}
