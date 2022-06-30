package com.orion.bitbucket.Bitbucket.controller;

import java.sql.SQLException;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
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
import com.orion.bitbucket.Bitbucket.model.ReviewDO.PullRequestReviewRelation;
import com.orion.bitbucket.Bitbucket.model.UserDO;
import com.orion.bitbucket.Bitbucket.service.AuthorServiceIF;
import com.orion.bitbucket.Bitbucket.service.BaseServiceIF;
import com.orion.bitbucket.Bitbucket.service.PullRequestServiceIF;
import com.orion.bitbucket.Bitbucket.service.ReviewServiceIF;
import com.orion.bitbucket.Bitbucket.service.ReviewerServiceIF;
import com.orion.bitbucket.Bitbucket.service.UserServiceIF;
import com.orion.bitbucket.Bitbucket.log.Log;
import com.orion.bitbucket.Bitbucket.dbc.DBConstants;
import org.springframework.web.bind.annotation.RequestParam;

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

    @Autowired
    private UserServiceIF userServiceIF;
    

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String allTime(Model model) throws UnirestException, SQLException {
        

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

        AuthorDO.TopAuthor topAuthorMerge = authorServiceIF.getTopAuthorAtMerged();
        model.addAttribute("topAuthorMerge", topAuthorMerge.getName());
        model.addAttribute("topAuthorMergePRsCount", topAuthorMerge.getTotal());

        AuthorDO.TopAuthor topAuthorOpen = authorServiceIF.getTopAuthorAtOpen();
        model.addAttribute("topAuthorOpen", topAuthorOpen.getName());
        model.addAttribute("topAuthorOpenPRsCount", topAuthorOpen.getTotal());

        AuthorDO.TopAuthor topAuthorDeclined = authorServiceIF.getTopAuthorAtDeclined();
        model.addAttribute("topAuthorDeclined", topAuthorDeclined.getName());
        model.addAttribute("topAuthorDeclinedPRsCount", topAuthorDeclined.getTotal());

        ArrayList<ReviewDO.PullRequestReviewRelation> mostOfPrReview = reviewServiceIF.mostReviewedPullRequest();
        model.addAttribute("size",mostOfPrReview.size());
            model.addAttribute("id", mostOfPrReview.get(0).getPullRequest().getPrId());
        
        return "index.html";
    }

    @RequestMapping(value = "/filter/{date}", method = RequestMethod.GET)
    public String filteringWithDateIndexPage(Model model, @PathVariable(name = "date", required = false) int date) throws UnirestException, SQLException {

        model.addAttribute("authorCount", authorServiceIF.getAuthorCount());
        model.addAttribute("pullRequestCount", pullRequestServiceIF.getAllPRCount());

        model.addAttribute("reviewerCount", reviewerServiceIF.getAllReviewerCount());
        model.addAttribute("reviewCount", reviewServiceIF.getTotalReviewCount());

        AuthorDO.TopAuthor topAuthor = authorServiceIF.getTopAuthorWithDateInterval(date);
        model.addAttribute("topAuthorDisplayName", topAuthor.getName());
        model.addAttribute("topAuthorPRsCount", topAuthor.getTotal());

         AuthorDO.TopAuthor topAuthorMerge = authorServiceIF.getTopAuthorWithDateIntervalAndState(date,"MERGED");
         model.addAttribute("topAuthorMerge", topAuthorMerge.getName());
         model.addAttribute("topAuthorMergePRsCount", topAuthorMerge.getTotal());

        AuthorDO.TopAuthor topAuthorOpen = authorServiceIF.getTopAuthorWithDateIntervalAndState(date,"OPEN");
        model.addAttribute("topAuthorOpen", topAuthorOpen.getName());
        model.addAttribute("topAuthorOpenPRsCount", topAuthorOpen.getTotal());

         AuthorDO.TopAuthor topAuthorDeclined = authorServiceIF.getTopAuthorWithDateIntervalAndState(date,"DECLINED");
         model.addAttribute("topAuthorDeclined", topAuthorDeclined.getName());
         model.addAttribute("topAuthorDeclinedPRsCount", topAuthorDeclined.getTotal());


        // TODO : No date filtering has been written yet, but will be written
        ReviewerDO.TopReviewer topReviewer = reviewerServiceIF.getTopReviewer();
        model.addAttribute("topReviewerDisplayName", topReviewer.getName());
        model.addAttribute("topReviewerCount",topReviewer.getTotal()); 


        // TODO : No date filtering has been written yet, but will be written
        ArrayList<ReviewDO.PullRequestReviewRelation> mostOfPrReview = reviewServiceIF.mostReviewedPullRequest();
        model.addAttribute("size",mostOfPrReview.size());
            model.addAttribute("id", mostOfPrReview.get(0).getPullRequest().getPrId());

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
    @RequestMapping(value = "/pull-requests/filter/")
    public String showPullRequestsPage(Model model,@RequestParam String startDate,@RequestParam String endDate )throws UnirestException, SQLException{

        try {
            ArrayList<AuthorDO> authors = authorServiceIF.getAllAuthorsUpdateWithFilter(startDate,endDate);

            model.addAttribute("author", new AuthorDO());
            model.addAttribute("authors",authors);
            return "pull-requests.html";

        }catch (DateTimeParseException dateTimeParseException){
            ArrayList<AuthorDO> authors = authorServiceIF.getAllAuthors();

            model.addAttribute("author", new AuthorDO());
            model.addAttribute("authors",authors);
            return  "pull-requests.html";
        }
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
        ArrayList<AuthorDO> authorDO = authorServiceIF.getCountPRStatesByUsername(name);
        model.addAttribute("author", new AuthorDO());
        model.addAttribute("getCountOfPrStates", authorDO);
        model.addAttribute("totalPRChart", authorDO.get(0).getTotalPRs());
        model.addAttribute("totalMergedChart", authorDO.get(0).getTotalMergedPRs());
        model.addAttribute("totalOpenChart", authorDO.get(0).getTotalOpenPRs());
        model.addAttribute("totalDeclinedChart", authorDO.get(0).getTotalDeclinedPRs());

       
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
            model.addAttribute("authorName",mergedList.get(0).getDisplayName());
            model.addAttribute("emailAddres",mergedList.get(0).getEmailAddress());
            model.addAttribute("slug", mergedList.get(0).getSlug());
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

    @RequestMapping(value = "/reviewer/{name}")
    public String showReviewerDetails(Model model, @PathVariable(name = "name", required = false) String name) throws UnirestException, SQLException {
        ArrayList<ReviewerDO> reviewerDO = reviewerServiceIF.getCountReviewStatesByUsername(name);
        model.addAttribute("reviewer", new ReviewerDO());
        model.addAttribute("getCount", reviewerDO);
        model.addAttribute("totalReviewChart", reviewerDO.get(0).getTotalReview());
        model.addAttribute("totalApproveChart", reviewerDO.get(0).getTotalApprove());
        model.addAttribute("totalUnapproveChart", reviewerDO.get(0).getTotalUnApprove());

        ArrayList<ReviewDO.PullRequestReviewRelation> reviewApproveList = reviewServiceIF.getReviewsByUsernameAndStatus(name, "APPROVED");
         model.addAttribute("relationApprove", new PullRequestReviewRelation());
         model.addAttribute("relationApproveList", reviewApproveList);

         ArrayList<ReviewDO.PullRequestReviewRelation> reviewUnapproveList = reviewServiceIF.getReviewsByUsernameAndStatus(name, "UNAPPROVED");
         model.addAttribute("relationUnapprove", new PullRequestReviewRelation());
         model.addAttribute("relationUnapproveList", reviewUnapproveList);

         if(reviewApproveList.isEmpty() != true) {
            model.addAttribute("reviewerName",reviewApproveList.get(0).getReview().getDisplayName());
            model.addAttribute("emailAddres",reviewApproveList.get(0).getReview().getEmailAddress());
            model.addAttribute("id", reviewApproveList.get(0).getReview().getId());
        }else{
            model.addAttribute("reviewerName",reviewUnapproveList.get(0).getReview().getDisplayName());
            model.addAttribute("emailAddres",reviewUnapproveList.get(0).getReview().getEmailAddress());
            model.addAttribute("id", reviewUnapproveList.get(0).getReview().getId());
        }

        return "reviewer-details.html";
    }

    @RequestMapping(value = "/pull-requests/{id}")
    public String showPullRequestDetails(Model model, @PathVariable(name = "id", required = false) int id) throws UnirestException, SQLException {
        PullRequestDO getPullRequest = pullRequestServiceIF.getPullRequestById(id);
        model.addAttribute("prId", getPullRequest.getPrId());
        model.addAttribute("title", getPullRequest.getTitle());
        model.addAttribute("description", getPullRequest.getDescription());
        model.addAttribute("name", getPullRequest.getDisplayName());
        model.addAttribute("state", getPullRequest.getState());
        model.addAttribute("createdDate", getPullRequest.getCreatedDate());
        model.addAttribute("closedDate", getPullRequest.getClosedDate());

        ArrayList<ReviewDO> reviewApprovedList = reviewServiceIF.getReviewsWithPullRequestIdAndStatus(id, "APPROVED");
        model.addAttribute("approveReviewer", new PullRequestReviewRelation());
        model.addAttribute("approveReviewerList", reviewApprovedList);

        ArrayList<ReviewDO> reviewUnapprovedList = reviewServiceIF.getReviewsWithPullRequestIdAndStatus(id, "UNAPPROVED");
        model.addAttribute("unapproveReviewer", new PullRequestReviewRelation());
        model.addAttribute("unapproveReviewerList", reviewUnapprovedList);
       
        
        return "pull-request-details.html";
    }
    @RequestMapping(value = "/users", method = RequestMethod.GET)
    public String getUser(Model model) throws UnirestException, SQLException {

        ArrayList<UserDO> users = userServiceIF.getAllUsers();
        List<String> options = new ArrayList<String>();

        options.add(DBConstants.User.USER_ROLE_ALL);
        options.add(DBConstants.User.USER_ROLE_ADMIN);
        options.add(DBConstants.User.USER_ROLE_LEADER);
        options.add(DBConstants.User.USER_ROLE_NORMAL);

        model.addAttribute("options", options);

        model.addAttribute("user", new UserDO());
        model.addAttribute("users", users);
        return "team-users.html";
    }
    @RequestMapping(value = "/users/", method = RequestMethod.GET)
    public String getUserAll(Model model,@RequestParam String role) throws UnirestException, SQLException {

        List<String> options = new ArrayList<String>();

        options.add(DBConstants.User.USER_ROLE_ALL);
        options.add(DBConstants.User.USER_ROLE_ADMIN);
        options.add(DBConstants.User.USER_ROLE_LEADER);
        options.add(DBConstants.User.USER_ROLE_NORMAL);

        model.addAttribute("options", options);

        if (role.equals(DBConstants.User.USER_ROLE_ALL)){
            ArrayList<UserDO> users = userServiceIF.getAllUsers();
            model.addAttribute("user", new UserDO());
            model.addAttribute("users", users);

        }else{
            ArrayList<UserDO> users = userServiceIF.getAllUserWithRole(role);
            model.addAttribute("user", new UserDO());
            model.addAttribute("users", users);
        }
        return "team-users.html";
    }

    @RequestMapping(value = "/users/{username}", method = RequestMethod.GET)
    public String showUserDetails(Model model, @PathVariable(name = "username", required = false) String username) throws UnirestException, SQLException {
        ArrayList<String> list = userServiceIF.getUserFirstAndLastName(username);
        String headerName = list.get(1) +"  "+list.get(0);
        model.addAttribute("headerName",headerName);

        UserDO user  = userServiceIF.getUserInformation(username);
        model.addAttribute("username",user.getUsername());
        model.addAttribute("firstname",user.getFirstname());
        model.addAttribute("lastname",user.getLastname());
        model.addAttribute("password",user.getPassword());
        model.addAttribute("email",user.getEmail());
        model.addAttribute("teamCode",user.getTeamCode());
        model.addAttribute("role",user.getRole());

           int totalPR = userServiceIF.getUserCountTotalPR(user.getUsername());
           model.addAttribute("totalPullRequest",totalPR);

           String reviewerName = user.getLastname()+", "+user.getFirstname();
           int totalReview = userServiceIF.getUserCountReview(reviewerName);
           model.addAttribute("totalReview",totalReview);

        return "user-details.html";
    }
    @RequestMapping(value = "/users/delete/", method = RequestMethod.GET)
    public String deleteUser(Model model,  @RequestParam String oldUsername) throws UnirestException, SQLException {

        userServiceIF.getDeleteUserWithUserName(oldUsername);

        return "redirect-page.html";
    }
    @RequestMapping(value = "/users/edit/", method = RequestMethod.GET)
    public String editUser(Model model, @RequestParam String username,@RequestParam String firstname,
                                        @RequestParam String lastname, @RequestParam String password,
                                        @RequestParam String email,   @RequestParam String teamCode,
                                        @RequestParam String role, @RequestParam String oldUsername) throws UnirestException, SQLException{
        userServiceIF.getPreconditionForUpdate(username,firstname,lastname,password,email,teamCode,role,oldUsername);
        return "redirect-page.html";
    }
    @RequestMapping(value = "/users/add/", method = RequestMethod.GET)
    public String addUser(Model model, @RequestParam String username,@RequestParam String firstname,
                                       @RequestParam String lastname, @RequestParam String password,
                                       @RequestParam String email,   @RequestParam String teamCode,
                                       @RequestParam String role) throws UnirestException, SQLException{
        userServiceIF.getCollectUserInformation(username,firstname,lastname,password,email,teamCode,role);
        return "redirect-page.html";
    }

}
