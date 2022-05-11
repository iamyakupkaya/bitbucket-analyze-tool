package com.orion.bitbucket.Bitbucket.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.orion.bitbucket.Bitbucket.model.AuthorDO;
import com.orion.bitbucket.Bitbucket.service.AuthorServiceIF;
import com.orion.bitbucket.Bitbucket.service.BaseServiceIF;
import com.orion.bitbucket.Bitbucket.service.PullRequestServiceIF;
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
    private ReviewerServiceIF reviewerServiceIF;
    

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String edit(Model model) throws UnirestException {
        baseService.getData(); // Bu metot uygulamanin baslangicinda cagirilmali ki listeler doldurulsun. Hangi kisim ilk calisacaksa bu orada cagirilmali.
        model.addAttribute("authorCount", authorServiceIF.getAuthorCount());
        model.addAttribute("pullRequestCount", pullRequestServiceIF.getAllPRCount());
        model.addAttribute("reviewerCount", reviewerServiceIF.getAllReviewerCount());
        model.addAttribute("reviewCount", reviewerServiceIF.getAllReviewCount());

        String topAuthorDisplayName =null;
        Long topAuthorPRsCount = null;
        for (Map.Entry<String, Long> topAuthor : authorServiceIF.getTopAuthor().entrySet()) {
            topAuthorDisplayName = topAuthor.getKey();
            topAuthorPRsCount = topAuthor.getValue();
            
        }
        model.addAttribute("topAuthorDisplayName", topAuthorDisplayName);
        model.addAttribute("topAuthorPRsCount",topAuthorPRsCount); 
        
        String topReviewerDisplayName = null;
        Long topReviewerCount = null;

        for (Map.Entry<String, Long> topReviewer : reviewerServiceIF.getTopReviewer().entrySet()) {
            topReviewerDisplayName = topReviewer.getKey();
            topReviewerCount = topReviewer.getValue();
            
        }

        model.addAttribute("topReviewerDisplayName", topReviewerDisplayName);
        model.addAttribute("topReviewerCount",topReviewerCount); 

        return "index.html";
    }

    @RequestMapping(value = "/pull-requests", method = RequestMethod.GET)
    public String getPullRequestsPage(Model model) throws UnirestException {
        List<AuthorDO> getAllAuthor = authorServiceIF.getCountOfPrStatesOfAllAuthor();
        model.addAttribute("author", new AuthorDO());
        model.addAttribute("authors", getAllAuthor);
        return "pull-requests.html";
    }
    


    @RequestMapping(value = "/api/web-controller/test/@username={userName}")
    public String showAuthorDetails(Model model, @PathVariable(name = "userName", required = false) String userName) throws UnirestException {
        // List<UserPrDetails> UserPrDetails = service.filterPRsByName(userName);
        // model.addAttribute("prId", new UserPrDetails());
        // model.addAttribute("UserPrDetails", UserPrDetails);

        return "modalBox.html";
    }


}
