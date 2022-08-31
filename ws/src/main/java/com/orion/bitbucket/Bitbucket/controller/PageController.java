package com.orion.bitbucket.Bitbucket.controller;

import java.sql.SQLException;
import java.time.format.DateTimeParseException;
import java.util.*;
import com.orion.bitbucket.Bitbucket.log.Log;
import com.orion.bitbucket.Bitbucket.service.*;
import com.orion.bitbucket.Bitbucket.security.AdministratorServiceIF;
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

    @Autowired
    private TeamServiceIF teamServiceIF;

    @Autowired
    private AdministratorServiceIF administratorServiceIF;

    private final boolean IS_PAGE_CONTROLLER_LOGGING = false;
    public void htmlTeamList(Model model) throws SQLException{
        List<String> htmlTeamList = teamServiceIF.getAllTeams();
        model.addAttribute("htmlTeamList", htmlTeamList);
    }
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String allTime(Model model) throws UnirestException, SQLException {
        htmlTeamList(model);
        String updateMsg = "updating";
        if(!AdminController.isUpdateRun){
            model.addAttribute("authorCount", authorServiceIF.getAuthorCount());
            model.addAttribute("pullRequestCount", pullRequestServiceIF.getAllPRCount());
            model.addAttribute("reviewerCount", reviewerServiceIF.getAllReviewerCount());
            model.addAttribute("reviewCount", reviewServiceIF.getTotalReviewCount());
        }
        else{
            model.addAttribute("authorCount", updateMsg);
            model.addAttribute("pullRequestCount", updateMsg);
            model.addAttribute("reviewerCount", updateMsg);
            model.addAttribute("reviewCount", updateMsg);
        }
        AuthorDO.TopAuthor topAuthor = authorServiceIF.getTopAuthor();
        model.addAttribute("topAuthorDisplayName", topAuthor.getName());
        model.addAttribute("topAuthorPRsCount", topAuthor.getTotal());

        ReviewerDO.TopReviewer topReviewer = reviewerServiceIF.getTopReviewer();
        model.addAttribute("topReviewerDisplayName", topReviewer.getName());
        model.addAttribute("topReviewerCount",topReviewer.getTotal()); 

        AuthorDO.TopAuthor topAuthorMerge = authorServiceIF.getTopAuthorAtMerged();
        model.addAttribute("topAuthorMerge", topAuthorMerge.getName());
        model.addAttribute("topAuthorMergePRsCount", topAuthorMerge.getTotal());

        return "index.html";
    }

    @RequestMapping(value = "/filter/{date}", method = RequestMethod.GET)
    public String filteringWithDateIndexPage(Model model, @PathVariable(name = "date", required = false) int date) throws UnirestException, SQLException {
        htmlTeamList(model);
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
        if (IS_PAGE_CONTROLLER_LOGGING){
            Log.logger(Log.LogConstant.TAG_INFO,"Author table is filling up");}
        authorServiceIF.getAllAuthor();
        if (IS_PAGE_CONTROLLER_LOGGING){
            Log.logger(Log.LogConstant.TAG_INFO,"Reviewer table is filling up");}
        reviewerServiceIF.getAllReviewer();
        if (IS_PAGE_CONTROLLER_LOGGING){
            Log.logger(Log.LogConstant.TAG_INFO,"User table is filling up");}
        if(administratorServiceIF.checkAdmin()){administratorServiceIF.setAdmin();}
        userServiceIF.insertUserTable();  // Automatically pulls data from table PullRequest
        return "fill-data.html";
    }

    @RequestMapping(value = "/pull-requests", method = RequestMethod.GET)
    public String getPullRequestsPage(Model model) throws UnirestException, SQLException {
        htmlTeamList(model);
        ArrayList<AuthorDO> authors = authorServiceIF.getAllAuthors();
        model.addAttribute("author", new AuthorDO());
        model.addAttribute("authors", authors);
        return "pull-requests.html";
    }
    @RequestMapping(value = "/pull-requests/filter/")
    public String showPullRequestsPage(Model model,@RequestParam String startDate,@RequestParam String endDate )throws UnirestException, SQLException{
        htmlTeamList(model);
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
        htmlTeamList(model);
        ArrayList<ReviewerDO> reviewers = reviewerServiceIF.getAllReviewers();
        model.addAttribute("reviewer", new ReviewerDO());
        model.addAttribute("reviewers", reviewers);
        return "reviewer.html";
    }
    @RequestMapping(value = "/pull-requests/author/{name}")
    public String showAuthorDetails(Model model, @PathVariable(name = "name", required = false) String name) throws UnirestException, SQLException {
        htmlTeamList(model);
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
        int pagination = 0;
        htmlTeamList(model);
        ArrayList<ReviewerDO> reviewerDO = reviewerServiceIF.getCountReviewStatesByUsername(name);
        model.addAttribute("reviewer", new ReviewerDO());
        model.addAttribute("getCount", reviewerDO);
        model.addAttribute("totalReviewChart", reviewerDO.get(0).getTotalReview());
        model.addAttribute("totalApproveChart", reviewerDO.get(0).getTotalApprove());
        model.addAttribute("totalUnapproveChart", reviewerDO.get(0).getTotalUnApprove());

        ArrayList<ReviewDO.PullRequestReviewRelation> reviewApproveList = reviewServiceIF.getReviewsByUsernameAndStatus(name, "APPROVED",pagination);
         model.addAttribute("relationApprove", new PullRequestReviewRelation());
         model.addAttribute("relationApproveList", reviewApproveList);

         ArrayList<ReviewDO.PullRequestReviewRelation> reviewUnapproveList = reviewServiceIF.getReviewsByUsernameAndStatus(name, "UNAPPROVED",pagination);
         model.addAttribute("relationUnapprove", new PullRequestReviewRelation());
         model.addAttribute("relationUnapproveList", reviewUnapproveList);

         if(reviewApproveList.isEmpty() != true) {
            model.addAttribute("reviewerName",reviewApproveList.get(0).getReview().getDisplayName());
            model.addAttribute("emailAddres",reviewApproveList.get(0).getReview().getEmailAddress());
            model.addAttribute("id", reviewServiceIF.reviewer_id());
        }else{
            model.addAttribute("reviewerName",reviewUnapproveList.get(0).getReview().getDisplayName());
            model.addAttribute("emailAddres",reviewUnapproveList.get(0).getReview().getEmailAddress());
            model.addAttribute("id", reviewServiceIF.reviewer_id());
        }

        return "reviewer-details.html";
    }
    @RequestMapping(value = "/reviewer/{name}/")
    public String showReviewerDetailsPagination(Model model, @PathVariable(name = "name", required = false) String name,
                                      @RequestParam(name = "page", required = false) int page)
            throws UnirestException, SQLException {
        int modelSize = 15;
        int offset = 0;
        offset = page * modelSize;
        htmlTeamList(model);
        ArrayList<ReviewerDO> reviewerDO = reviewerServiceIF.getCountReviewStatesByUsername(name);
        model.addAttribute("reviewer", new ReviewerDO());
        model.addAttribute("getCount", reviewerDO);
        model.addAttribute("totalReviewChart", reviewerDO.get(0).getTotalReview());
        model.addAttribute("totalApproveChart", reviewerDO.get(0).getTotalApprove());
        model.addAttribute("totalUnapproveChart", reviewerDO.get(0).getTotalUnApprove());

        ArrayList<ReviewDO.PullRequestReviewRelation> reviewApproveList = reviewServiceIF.getReviewsByUsernameAndStatus(name, "APPROVED", offset);
        model.addAttribute("relationApprove", new PullRequestReviewRelation());
        model.addAttribute("relationApproveList", reviewApproveList);

        ArrayList<ReviewDO.PullRequestReviewRelation> reviewUnapproveList = reviewServiceIF.getReviewsByUsernameAndStatus(name, "UNAPPROVED", offset);
        model.addAttribute("relationUnapprove", new PullRequestReviewRelation());
        model.addAttribute("relationUnapproveList", reviewUnapproveList);

        if(reviewApproveList.isEmpty() != true) {
            model.addAttribute("reviewerName",reviewApproveList.get(0).getReview().getDisplayName());
            model.addAttribute("emailAddres",reviewApproveList.get(0).getReview().getEmailAddress());
            model.addAttribute("id", reviewServiceIF.reviewer_id());
        }else{
            model.addAttribute("reviewerName",reviewUnapproveList.get(0).getReview().getDisplayName());
            model.addAttribute("emailAddres",reviewUnapproveList.get(0).getReview().getEmailAddress());
            model.addAttribute("id", reviewServiceIF.reviewer_id());
        }

        return "reviewer-details.html";
    }

    @RequestMapping(value = "/pull-requests/{id}")
    public String showPullRequestDetails(Model model, @PathVariable(name = "id", required = false) int id) throws UnirestException, SQLException {
        htmlTeamList(model);
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
        htmlTeamList(model);
        List<String> roles = userServiceIF.getRoles();
        roles.add(0,DBConstants.User.USER_ROLE_ALL);
        model.addAttribute("roles", roles);

        List<String> teams = teamServiceIF.getAllTeams();
        teams.add(0,DBConstants.User.USERS_TEAM_ALL);
        model.addAttribute("teams", teams);

        ArrayList<UserDO> users = userServiceIF.getAllUsers();
        model.addAttribute("user", new UserDO());
        model.addAttribute("users", users);
        return "users.html";
    }
    @RequestMapping(value = "/users/", method = RequestMethod.GET)
    public String getUserAll(Model model,@RequestParam String role, @RequestParam String team) throws UnirestException, SQLException {
        htmlTeamList(model);
        List<String> roles = userServiceIF.getRoles();
        roles.add(0,DBConstants.User.USER_ROLE_ALL);
        model.addAttribute("roles", roles);

        List<String> teams = teamServiceIF.getAllTeams();
        teams.add(0,DBConstants.User.USERS_TEAM_ALL);
        model.addAttribute("teams", teams);

        if (role.equals(DBConstants.User.USER_ROLE_ALL) && team.equals(DBConstants.User.USERS_TEAM_ALL) ) {
            ArrayList<UserDO> users = userServiceIF.getAllUsers();
            model.addAttribute("user", new UserDO());
            model.addAttribute("users", users);
        }else if(team.equals(DBConstants.User.USERS_TEAM_ALL)){
            ArrayList<UserDO> users = userServiceIF.getAllUserWithRole(role);
            model.addAttribute("user", new UserDO());
            model.addAttribute("users", users);
        }else if(role.equals(DBConstants.User.USER_ROLE_ALL)) {
            ArrayList<UserDO> users = userServiceIF.getAllUserWithTeam(team);
            model.addAttribute("user", new UserDO());
            model.addAttribute("users", users);
        }else{
            ArrayList<UserDO> users = userServiceIF.getAllUserWithRoleAndTeam(role,team);
            model.addAttribute("user", new UserDO());
            model.addAttribute("users", users);
        }
        return "users.html";
    }

    @RequestMapping(value = "/users/{username}", method = RequestMethod.GET)
    public String showUserDetails(Model model, @PathVariable(name = "username", required = false) String username) throws UnirestException, SQLException {
        htmlTeamList(model);
        ArrayList<String> list = userServiceIF.getUserFirstAndLastName(username);
        String headerName = list.get(1) +"  "+list.get(0);
        model.addAttribute("headerName",headerName);

        UserDO user  = userServiceIF.getUserInformation(username);
        model.addAttribute("username",user.getUsername());
        model.addAttribute("firstname",user.getFirstname());
        model.addAttribute("lastname",user.getLastname());
        model.addAttribute("email",user.getEmail());
        model.addAttribute("teamCode",user.getTeamCode());
        model.addAttribute("role",user.getRole());

        String mergeName = null;
        if (user.getLastname().length() > 0) {
            mergeName = user.getLastname() + ", " + user.getFirstname();
        }else{
            mergeName = user.getFirstname();
        }

           int totalPR = userServiceIF.getUserCountTotalPR(user.getUsername());
           model.addAttribute("authorName",mergeName);
           model.addAttribute("totalPullRequest",totalPR);

           int totalReview = userServiceIF.getUserCountReview(mergeName);
           model.addAttribute("reviewerName",mergeName);
           model.addAttribute("totalReview",totalReview);

        return "user-details.html";
    }
    @RequestMapping(value = "/team/{teamCode}", method = RequestMethod.GET)
    public String teamCodePage(Model model, @PathVariable(name = "teamCode", required = false) String teamCode) throws UnirestException, SQLException {
        htmlTeamList(model);
        ArrayList<UserDO> teamMembers = teamServiceIF.getTeamUsers(teamCode);
        ArrayList<String> namesList = null;
        ArrayList<AuthorDO> teamUsersStatistics = null;
        ArrayList<ReviewerDO> teamReviewerStatistics = null;
        namesList = new ArrayList<String>();

        String name = null;
        for (int i = 0; i<teamMembers.size(); i++){

            if (teamMembers.get(i).getLastname().length() > 0) {
                name = teamMembers.get(i).getLastname() +", " +teamMembers.get(i).getFirstname();
                namesList.add(name);
            }else{
                name = teamMembers.get(i).getFirstname();
                namesList.add(name);
            }
        }
            teamUsersStatistics = teamServiceIF.getTeamUsersStatistics(namesList);
            model.addAttribute("teamUsersStatistic",new AuthorDO());
            model.addAttribute("teamUsersStatistics",teamUsersStatistics);

            teamReviewerStatistics = teamServiceIF.getTeamReviewerStatistics(namesList);
            model.addAttribute("teamReviewerStatistics",new ReviewerDO());
            model.addAttribute("teamReviewerStatistics",teamReviewerStatistics);

            int totalTeamPR = teamServiceIF.getTeamsTotalPR();
            model.addAttribute("totalTeamPR", totalTeamPR);

            int totalReviewer = teamServiceIF.getTeamsTotalReview();
            model.addAttribute("totalReviewer", totalReviewer);

            model.addAttribute("headerTeamCode",teamCode);
            model.addAttribute("manager", teamServiceIF.getTeamManagerTitle(teamCode));

            ArrayList<UserDO> users = userServiceIF.getAllUserWithTeam(teamCode);
            model.addAttribute("user", new UserDO());
            model.addAttribute("users", users);

        return "team.html";
    }
}
