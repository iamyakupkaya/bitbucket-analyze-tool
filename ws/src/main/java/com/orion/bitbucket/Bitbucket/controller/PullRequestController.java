package com.orion.bitbucket.Bitbucket.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import com.orion.bitbucket.Bitbucket.service.PullRequestService;

@RestController
public class PullRequestController {

    @Autowired
    private PullRequestService pullRequestService;


    // @RequestMapping("/api/pull-requests/all/{userName}")
    // public List<UserPrDetails> testPrs(@PathVariable String name) throws UnirestException{
    //    return pullRequestService.filterPRsByName(name);
    // }


    // @RequestMapping("/api/pull-requests/open")
    // public String getOpenPullRequests() throws JsonSyntaxException, UnirestException {
    //    return pullRequestService.open();
    // }
}

