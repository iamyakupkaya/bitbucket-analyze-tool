
package com.orion.bitbucket.Bitbucket.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.JsonSyntaxException;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.orion.bitbucket.Bitbucket.model.PullRequest;
import com.orion.bitbucket.Bitbucket.model.UserPrDetails;

import com.orion.bitbucket.Bitbucket.service.InspectionService;
import com.orion.bitbucket.Bitbucket.service.PullRequestService;

@RestController
public class PullRequestController {
   
   @Autowired
   private PullRequestService pullRequestService;

   @RequestMapping("/api/pull-requests/all")
   public String getAllPullRequests() throws UnirestException {
      return pullRequestService.all();
   }
   
   @RequestMapping("/api/pull-requests/all/{userName}")
   public List<UserPrDetails> testPrs(@PathVariable String name) throws UnirestException{
	   return pullRequestService.filterPRsByName(name);
   }
   
   
   @RequestMapping("/api/pull-requests/open")
   public String getOpenPullRequests() throws JsonSyntaxException, UnirestException {
      return pullRequestService.open();
   }
   
   @RequestMapping("/api/pull-requests/merged")
   public String mergedPullRequests() throws JsonSyntaxException, UnirestException {
      return pullRequestService.merged();
   }
   
   @RequestMapping("/api/pull-requests/declined")
   public String declinedPullRequests() throws JsonSyntaxException, UnirestException {
      return pullRequestService.declined();
   }
   
   @RequestMapping("/api/pull-requests/declinedddd")
   public List<PullRequest> getALLTEST() throws JsonSyntaxException, UnirestException {
      return pullRequestService.reportAll();
   }
   
   @RequestMapping("/api/pull-requests/testauthor")
   public List<PullRequest> getAllAuthorTest() throws JsonSyntaxException, UnirestException {
      return pullRequestService.reportAllAuthorTest();
   }
   
}

