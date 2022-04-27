package com.orion.bitbucket.Bitbucket.service;


import com.mashape.unirest.http.exceptions.UnirestException;

public interface ServiceIF {
   
   public String getPullRequests(String status, String jsonPath) throws UnirestException;
   public String getApprovals(String status, String reviewerPath, String approvePath) throws  UnirestException;
}
