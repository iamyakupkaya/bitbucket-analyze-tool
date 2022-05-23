package com.orion.bitbucket.Bitbucket.controller;

import java.sql.SQLException;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.orion.bitbucket.Bitbucket.model.AuthorDO;
import com.orion.bitbucket.Bitbucket.model.PullRequestDO;
import com.orion.bitbucket.Bitbucket.model.ReviewerDO;
import com.orion.bitbucket.Bitbucket.service.AuthorServiceIF;
import com.orion.bitbucket.Bitbucket.service.BaseServiceIF;
import com.orion.bitbucket.Bitbucket.service.PullRequestServiceIF;
import com.orion.bitbucket.Bitbucket.service.ReviewServiceIF;
import com.orion.bitbucket.Bitbucket.service.ReviewerServiceIF;

@Controller
public class PageController {

    @Autowired
    private BaseServiceIF baseService;

    @Autowired
    private PullRequestServiceIF pullRequestServiceIF;

    @Autowired
    private AuthorServiceIF authorServiceIF;

    @Autowired
    private ReviewServiceIF reviewServiceIF;

    @Autowired
    private ReviewerServiceIF reviewerServiceIF;
    

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String edit(Model model) throws UnirestException, SQLException {

        

        model.addAttribute("authorCount", authorServiceIF.getAuthorCount());
        model.addAttribute("pullRequestCount", pullRequestServiceIF.getAllPRCount());

        model.addAttribute("reviewerCount", reviewerServiceIF.getAllReviewerCount());
        model.addAttribute("reviewCount", reviewServiceIF.getTotalReviewCount());

        AuthorDO.TopAuthor topAuthor = authorServiceIF.getTopAuthor();
        model.addAttribute("topAuthorDisplayName", topAuthor.getName());
        model.addAttribute("topAuthorPRsCount", topAuthor.getTotal());

        ReviewerDO.TopReviewer topReviewer = reviewerServiceIF.getTopReviewer();
        model.addAttribute("topReviewerDisplayName", topReviewer.getName());
        model.addAttribute("topReviewerCount",topReviewer.getTotal()); 

        return "index.html";
    }

    @RequestMapping(value = "/fill-data", method = RequestMethod.GET)
    public String getFillDataPage(Model model) throws UnirestException, SQLException {
        baseService.getData();
        authorServiceIF.getAllAuthor();
        reviewerServiceIF.getAllReviewer();
        return "fill-data.html";
    }

    @RequestMapping(value = "/pull-requests", method = RequestMethod.GET)
    public String getPullRequestsPage(Model model) throws UnirestException, SQLException {
        ArrayList<AuthorDO> authors = authorServiceIF.getAllAuthors();

        model.addAttribute("author", new AuthorDO());
        model.addAttribute("authors", authors);
        return "pull-requests.html";
    }

    @RequestMapping(value = "/reviewer", method = RequestMethod.GET)
    public String getReviewPage(Model model) throws UnirestException, SQLException {
        ArrayList<ReviewerDO> reviewers = reviewerServiceIF.getAllReviewers();
        
        model.addAttribute("reviewer", new ReviewerDO());
        model.addAttribute("reviewers", reviewers);
        return "reviewer.html";
    }
    


    @RequestMapping(value = "/pull-requests/author/{name}")
    public String showAuthorDetails(Model model, @PathVariable(name = "name", required = false) String name) throws UnirestException, SQLException {
        ArrayList<AuthorDO> authorDO = authorServiceIF.getCountOfPrStatesWithDisplayName(name);
        model.addAttribute("author", new AuthorDO());
        model.addAttribute("getCountOfPrStates", authorDO);
       
        ArrayList<PullRequestDO> mergedList = pullRequestServiceIF.getPRListByStateAndUsername("MERGED",name);
        model.addAttribute("merged", new PullRequestDO());
        model.addAttribute("mergedList", mergedList);
        
        ArrayList<PullRequestDO> openList = pullRequestServiceIF.getPRListByStateAndUsername("OPEN",name);
        model.addAttribute("open", new PullRequestDO());
        model.addAttribute("openList", openList);

        ArrayList<PullRequestDO> declinedList = pullRequestServiceIF.getPRListByStateAndUsername("DECLINED",name);
        model.addAttribute("declined", new PullRequestDO());
        model.addAttribute("declinedList", declinedList);

        if(mergedList.isEmpty() != true) {
            model.addAttribute("authorName",openList.get(0).getDisplayName());
            model.addAttribute("emailAddres",openList.get(0).getEmailAddress());
            model.addAttribute("slug", openList.get(0).getSlug());
        }else if(openList.isEmpty() != true) {
            model.addAttribute("authorName",openList.get(0).getDisplayName());
            model.addAttribute("emailAddres",openList.get(0).getEmailAddress());
            model.addAttribute("slug", openList.get(0).getSlug());
        }else {
            model.addAttribute("authorName",declinedList.get(0).getDisplayName());
            model.addAttribute("emailAddres",declinedList.get(0).getEmailAddress());
            model.addAttribute("slug", declinedList.get(0).getSlug());
        }
        
        return "author-details.html";
    }
  


}
