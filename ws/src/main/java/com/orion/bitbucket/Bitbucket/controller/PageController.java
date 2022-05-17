package com.orion.bitbucket.Bitbucket.controller;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.orion.bitbucket.Bitbucket.model.AuthorDO;
import com.orion.bitbucket.Bitbucket.model.PullRequestDO;
import com.orion.bitbucket.Bitbucket.model.ReviewDO;
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
        baseService.getData();
        authorServiceIF.getTopAuthor();
        model.addAttribute("authorCount", authorServiceIF.getAuthorCount());
        model.addAttribute("pullRequestCount", pullRequestServiceIF.getAllPRCount());

        //model.addAttribute("reviewerCount", reviewServiceIF.getAllReviewerCount());
        //model.addAttribute("reviewCount", reviewServiceIF.getAllReviewCount());

        AuthorDO.TopAuthor topAuthor = authorServiceIF.getTopAuthor();
        model.addAttribute("topAuthorDisplayName", topAuthor.getName());
        model.addAttribute("topAuthorPRsCount", topAuthor.getTotal());

          /*
        String topReviewerDisplayName = null;
        Long topReviewerCount = null;

        for (Map.Entry<String, Long> topReviewer : reviewServiceIF.getTopReviewer().entrySet()) {
            topReviewerDisplayName = topReviewer.getKey();
            topReviewerCount = topReviewer.getValue();
            
        }

        model.addAttribute("topReviewerDisplayName", topReviewerDisplayName);
        model.addAttribute("topReviewerCount",topReviewerCount); 
*/
        return "index.html";
    }

    @RequestMapping(value = "/pull-requests", method = RequestMethod.GET)
    public String getPullRequestsPage(Model model) throws UnirestException, SQLException {
        ArrayList<AuthorDO> authors = authorServiceIF.getAllAuthors();

        model.addAttribute("author", new AuthorDO());
        model.addAttribute("authors", authors);
        return "pull-requests.html";
    }

    @RequestMapping(value = "/review", method = RequestMethod.GET)
    public String getReviewPage(Model model) throws UnirestException {
        ArrayList<ReviewerDO> getAllReviewer = reviewerServiceIF.getCountOfReviewStatesOfAllReviewer();
        model.addAttribute("reviewer", new ReviewerDO());
        model.addAttribute("reviewers", getAllReviewer);
        return "pull-requests.html";
    }
    


    @RequestMapping(value = "/pull-requests/author/{name}")
    public String showAuthorDetails(Model model, @PathVariable(name = "name", required = false) String name) throws UnirestException {
        //ArrayList<AuthorDO> authorDO = authorServiceIF.getCountOfPrStatesWithDisplayName(name);
        //model.addAttribute("author", new AuthorDO());
        //model.addAttribute("getCountOfPrStates", authorDO);

        //ArrayList<PullRequestDO> mergedList = pullRequestServiceIF.getMergedPRListByUsername(name);
        //model.addAttribute("merged", new PullRequestDO());
        //model.addAttribute("mergedList", mergedList);
        ArrayList<ReviewDO> reviewerList = new ArrayList<ReviewDO>();


        //ArrayList<PullRequestDO> openList = pullRequestServiceIF.getOpenPRListByUsername(name);
        //model.addAttribute("open", new PullRequestDO());
        //model.addAttribute("openList", openList);

        //ArrayList<PullRequestDO> declinedList = pullRequestServiceIF.getDeclinedPRListByUsername(name);
        //model.addAttribute("declined", new PullRequestDO());
        //model.addAttribute("declinedList", declinedList);
        
       
        return "author-details.html";
    }
  


}
