package com.orion.bitbucket.Bitbucket.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

import com.mashape.unirest.http.exceptions.UnirestException;
import com.orion.bitbucket.Bitbucket.model.AuthorDO;
import com.orion.bitbucket.Bitbucket.service.AuthorServiceIF;
import com.orion.bitbucket.Bitbucket.service.PullRequestService;

@RestController
public class PullRequestController {

    @Autowired
    private PullRequestService pullRequestService;

    @Autowired
    private AuthorServiceIF authorServiceIF;


     @RequestMapping("/api/pull-requests/all/{name}")
     public ArrayList<AuthorDO> testPrs(@PathVariable String name) throws UnirestException{
        return authorServiceIF.getCountOfPrStatesWithDisplayName(name);
     }


    // @RequestMapping("/api/pull-requests/open")
    // public String getOpenPullRequests() throws JsonSyntaxException, UnirestException {
    //    return pullRequestService.open();
    // }
}

