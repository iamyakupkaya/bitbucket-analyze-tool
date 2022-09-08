package com.orion.bitbucket.Bitbucket.controller;

import com.mashape.unirest.http.exceptions.UnirestException;
import com.orion.bitbucket.Bitbucket.dbc.DBConstants;
import com.orion.bitbucket.Bitbucket.model.AuthorDO;
import com.orion.bitbucket.Bitbucket.model.PullRequestDO;
import com.orion.bitbucket.Bitbucket.model.ReviewerDO;
import com.orion.bitbucket.Bitbucket.model.UserDO;
import com.orion.bitbucket.Bitbucket.security.AdministratorServiceIF;
import com.orion.bitbucket.Bitbucket.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import java.sql.SQLException;
import java.util.*;

@Controller
public class AdminController {
    @Autowired
    private UserServiceIF userServiceIF;
    @Autowired
    private TeamServiceIF teamServiceIF;
    @Autowired
    private AdministratorServiceIF administratorServiceIF;
    @Autowired
    private UpdateServiceIF updateServiceIF;
    public static boolean isAutoUpdateTemp = false;
    public static boolean isUpdateRun = false;
    private Timer timer;
    private String updateMsg= "Automatically update";
    private String updateMsgTime;
    public void htmlTeamList(Model model) throws SQLException{
        List<String> htmlTeamList = teamServiceIF.getAllTeams();
        model.addAttribute("htmlTeamList", htmlTeamList);
    }

    @RequestMapping(value = "/administrator", method = RequestMethod.GET)
    public String administrator(Model model) throws UnirestException, SQLException {
        htmlTeamList(model);
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String loginName = null;
        if (principal instanceof UserDetails) {
            loginName = ((UserDetails)principal).getUsername();
        } else {
            loginName = principal.toString();
        }
        model.addAttribute("loginName",loginName);

        List<String> teamSet = teamServiceIF.getAllTeams();
        teamSet.add(0, DBConstants.User.DEFAULT_USERS_TEAM_EMPTY);
        model.addAttribute("teamSet", teamSet);

        List<String> teams = teamServiceIF.getAllTeams();
        model.addAttribute("teams", teams);

        ArrayList<UserDO> allUsers = userServiceIF.getAllUsers();
        model.addAttribute("allUser",new UserDO());
        model.addAttribute("allUsers", allUsers);

        ArrayList<String> leaders = teamServiceIF.getAllUserWithRole(true);
        model.addAttribute("leaders",leaders);

        ArrayList<String> usersAssign = teamServiceIF.getAllUserWithRole(false);
        model.addAttribute("usersAssign",usersAssign);


        List<String> rolesFilter = userServiceIF.getRoles();
        rolesFilter.add(0,DBConstants.User.USER_ROLE_ALL);
        model.addAttribute("rolesFilter", rolesFilter);

        List<String> teamsFilter = teamServiceIF.getAllTeams();
        teamsFilter.add(0,DBConstants.User.USERS_TEAM_ALL);
        model.addAttribute("teamsFilter", teamsFilter);

        model.addAttribute("updateToggle",isAutoUpdateTemp);
        model.addAttribute("updateMsg",updateMsg);
        model.addAttribute("updateMsgTime",updateMsgTime);

        if(!(updateServiceIF.updateDetail() == null)){
            ArrayList<PullRequestDO> updateDetails = updateServiceIF.updateDetail();
            model.addAttribute("updateDetail", new PullRequestDO());
            model.addAttribute("updateDetails", updateDetails);
        }
        return "admin-panel.html";
    }
    @RequestMapping(value = "administrator/", method = RequestMethod.GET)
    public String getAllUser(Model model,@RequestParam String role, @RequestParam String team) throws UnirestException, SQLException {
        htmlTeamList(model);
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String loginName = null;
        if (principal instanceof UserDetails) {
            loginName = ((UserDetails)principal).getUsername();
        } else {
            loginName = principal.toString();
        }
        model.addAttribute("loginName",loginName);

        List<String> teamSet = teamServiceIF.getAllTeams();
        teamSet.add(0, DBConstants.User.DEFAULT_USERS_TEAM_EMPTY);
        model.addAttribute("teamSet", teamSet);

        List<String> teams = teamServiceIF.getAllTeams();
        model.addAttribute("teams", teams);

        ArrayList<String> leaders = teamServiceIF.getAllUserWithRole(true);
        model.addAttribute("leaders",leaders);

        ArrayList<String> usersAssign = teamServiceIF.getAllUserWithRole(false);
        model.addAttribute("usersAssign",usersAssign);

        List<String> rolesFilter = userServiceIF.getRoles();
        rolesFilter.add(0,DBConstants.User.USER_ROLE_ALL);
        model.addAttribute("rolesFilter", rolesFilter);

        List<String> teamsFilter = teamServiceIF.getAllTeams();
        teamsFilter.add(0,DBConstants.User.USERS_TEAM_ALL);
        model.addAttribute("teamsFilter", teamsFilter);

        model.addAttribute("updateToggle",isAutoUpdateTemp);
        model.addAttribute("updateMsg",updateMsg);
        model.addAttribute("updateMsgTime",updateMsgTime);

        if (role.equals(DBConstants.User.USER_ROLE_ALL) && team.equals(DBConstants.User.USERS_TEAM_ALL) ) {
            ArrayList<UserDO> allUsers = userServiceIF.getAllUsers();
            model.addAttribute("allUser", new UserDO());
            model.addAttribute("allUsers", allUsers);
        }else if(team.equals(DBConstants.User.USERS_TEAM_ALL)){
            ArrayList<UserDO> allUsers = userServiceIF.getAllUserWithRole(role);
            model.addAttribute("allUser", new UserDO());
            model.addAttribute("allUsers", allUsers);
        }else if(role.equals(DBConstants.User.USER_ROLE_ALL)) {
            ArrayList<UserDO> allUsers = userServiceIF.getAllUserWithTeam(team);
            model.addAttribute("allUser", new UserDO());
            model.addAttribute("allUsers", allUsers);
        }else{
            ArrayList<UserDO> allUsers = userServiceIF.getAllUserWithRoleAndTeam(role,team);
            model.addAttribute("allUser", new UserDO());
            model.addAttribute("allUsers", allUsers);
        }
        return "admin-panel.html";
    }
    @RequestMapping(value = "administrator/team/insert", method = RequestMethod.GET)
    public String getTeamInsert(Model model, @RequestParam String teamCode) throws SQLException {
        List<String> teams = teamServiceIF.getAllTeams();
        model.addAttribute("teams", teams);
        if(teams.size() == 0){
            teamServiceIF.insertTeams(teamCode);
        }else{
            for(String team:teams){
                if(teamCode.contains(team)){
                    return "redirect:/administrator";
                }
            }
            teamServiceIF.insertTeams(teamCode);
        }
        return "redirect:/administrator";
    }
    @RequestMapping(value = "administrator/administrator/team/insert", method = RequestMethod.GET)
    public String getTeamInsertFilter(Model model, @RequestParam String teamCode) throws SQLException {
        List<String> teams = teamServiceIF.getAllTeams();
        model.addAttribute("teams", teams);
        if(teams.size() == 0){
            teamServiceIF.insertTeams(teamCode);
        }else{
            for(String team:teams){
                if(teamCode.contains(team)){
                    return "redirect:/administrator";
                }
            }
            teamServiceIF.insertTeams(teamCode);
        }
        return "redirect:/administrator";
    }
    @RequestMapping(value = "administrator/team/delete", method = RequestMethod.GET)
    public String getTeamDelete(Model model, @RequestParam String teamCode) throws SQLException {
        teamServiceIF.deleteTeam(teamCode);
        return "redirect:/administrator";
    }
    @RequestMapping(value = "administrator/administrator/team/delete", method = RequestMethod.GET)
    public String getTeamDeleteFilter(Model model, @RequestParam String teamCode) throws SQLException {
        teamServiceIF.deleteTeam(teamCode);
        return "redirect:/administrator";
    }
    @RequestMapping(value = "administrator/user/add/", method = RequestMethod.GET)
    public String addUser(Model model, @RequestParam String username,@RequestParam String firstname,
                          @RequestParam String lastname, @RequestParam String email) throws  SQLException{
        String role = DBConstants.User.USER_ROLE_USER;
        String teamCode = DBConstants.User.DEFAULT_USERS_TEAM_EMPTY;
        String password = DBConstants.User.DEFAULT_USER_PASSWORD;
        userServiceIF.getCollectUserInformation(username,firstname,lastname,password,email,teamCode,role);
        return "redirect:/administrator";
    }
    @RequestMapping(value = "administrator/administrator/user/add/", method = RequestMethod.GET)
    public String addUserFilter(Model model, @RequestParam String username,@RequestParam String firstname,
                          @RequestParam String lastname, @RequestParam String email) throws  SQLException{
        String role = DBConstants.User.USER_ROLE_USER;
        String teamCode = DBConstants.User.DEFAULT_USERS_TEAM_EMPTY;
        String password = DBConstants.User.DEFAULT_USER_PASSWORD;
        userServiceIF.getCollectUserInformation(username,firstname,lastname,password,email,teamCode,role);
        return "redirect:/administrator";
    }
    @RequestMapping(value = "/administrator/user/setTeam/", method = RequestMethod.GET)
    public String setTeamUser(Model model,@RequestParam String username,@RequestParam String teamCode) throws  SQLException{
        teamServiceIF.setDefaultAuthority(username);
        teamServiceIF.updateTeamMembersWithUsername(username,teamCode);
        return "redirect:/administrator";
    }
    @RequestMapping(value = "administrator/administrator/user/setTeam/", method = RequestMethod.GET)
    public String setTeamUserFilter(Model model,@RequestParam String username,@RequestParam String teamCode) throws  SQLException{
        teamServiceIF.setDefaultAuthority(username);
        teamServiceIF.updateTeamMembersWithUsername(username,teamCode);
        return "redirect:/administrator";
    }
    public void teamCodePage(Model model, String teamCode) throws SQLException{
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

        List<String> roles = userServiceIF.getRoles();
        model.addAttribute("roles", roles);
    }
    @RequestMapping(value = "administrator/team/{teamCode}")
    public String setTeamUserRole(Model model, @PathVariable(name = "teamCode", required = false) String teamCode) throws UnirestException,SQLException{
        htmlTeamList(model);
        teamCodePage(model,teamCode);
        return "admin-team.html";
    }
    @RequestMapping(value = "administrator/team/{teamCode}/setRole", method = RequestMethod.GET)
    public String setTeamUserRole(Model model, @PathVariable(name = "teamCode", required = false) String teamCode,@RequestParam String role,@RequestParam String username) throws  SQLException{
        teamCodePage(model,teamCode);
        teamServiceIF.getTeamRoleAssign(teamCode,username,role);
        return "redirect:/administrator/team/{teamCode}/";
    }
    @RequestMapping(value = "administrator/setAssign", method = RequestMethod.GET)
    public String setAssign(Model model, @RequestParam String username) throws  SQLException{
        teamServiceIF.getUserRoleLeader(username,true);
        if(administratorServiceIF.checkAdmin(username)){teamServiceIF.setAuthorityLeader(username);}
        return "redirect:/administrator";
    }
    @RequestMapping(value = "administrator/administrator/setAssign", method = RequestMethod.GET)
    public String setAssignFilter(Model model, @RequestParam String username) throws  SQLException{
        teamServiceIF.getUserRoleLeader(username,true);
        if(administratorServiceIF.checkAdmin(username)){teamServiceIF.setAuthorityLeader(username);}
        return "redirect:/administrator";
    }
    @RequestMapping(value = "administrator/undoAssign", method = RequestMethod.GET)
    public String undoAssign(Model model, @RequestParam String username) throws  SQLException{
        teamServiceIF.getUserRoleLeader(username,false);
        teamServiceIF.setDefaultAuthority(username);
        return "redirect:/administrator";
    }
    @RequestMapping(value = "administrator/administrator/undoAssign", method = RequestMethod.GET)
    public String undoAssignFilter(Model model, @RequestParam String username) throws  SQLException{
        teamServiceIF.getUserRoleLeader(username,false);
        teamServiceIF.setDefaultAuthority(username);
        return "redirect:/administrator";
    }
    @RequestMapping(value = "administrator/user/{username}", method = RequestMethod.GET)
    public String showUserDetailsAdmin(Model model, @PathVariable(name = "username", required = false) String username) throws UnirestException, SQLException {
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

        return "admin-user-details.html";
    }
    @RequestMapping(value = "administrator/administrator/user/{username}", method = RequestMethod.GET)
    public String showUserDetailsAdminFilter(Model model, @PathVariable(name = "username", required = false) String username) throws UnirestException, SQLException {
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

        return "redirect:/administrator/user/{username}";
    }
    @RequestMapping(value = "administrator/user/edit", method = RequestMethod.GET)
    public String editUser(@RequestParam String username,@RequestParam String firstname,
                           @RequestParam String lastname, @RequestParam String email) throws UnirestException, SQLException{
        userServiceIF.getUpdateUserInformation(firstname,lastname,email,username);
        return "redirect:/administrator/user/" + username;
    }
    @RequestMapping(value = "administrator/user/delete", method = RequestMethod.GET)
    public String deleteUser(Model model, @RequestParam String username) throws UnirestException, SQLException {
        administratorServiceIF.deleteAuthority(username);
        userServiceIF.getDeleteUserWithUserName(username);
        return "redirect:/administrator";
    }
    @RequestMapping(value = "/administrator/autoUpdate/", method = RequestMethod.GET)
    public String autoUpdate(@RequestParam String toggle) throws UnirestException, SQLException {

        boolean isAutoUpdateToggle = Boolean.parseBoolean(toggle);
        isAutoUpdateTemp = isAutoUpdateToggle;

        if(isAutoUpdateToggle){autoUpdate(true);}
        else{autoUpdate(false);}

        return "redirect:/administrator";
    }
    public void autoUpdate(boolean time){
        Calendar firstTaskTime = getFirstTime();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                isUpdateRun = true;
//                System.out.println("Bitbucket analyze tool is updating");
                updateServiceIF.runUpdate();
//                System.out.println("Update is done. Next update will start at:" + getFirstTime().getTime());
                isUpdateRun = false;
                updateMsg = "Update is done. Next update will start at:";
                updateMsgTime = String.valueOf(getFirstTime().getTime());
            }
        };
        if(time){
            timer = new Timer();
//            System.out.println("Updating will start at: " + firstTaskTime.getTime());
            timer.schedule(timerTask, firstTaskTime.getTime(), 1000 * 60 * 15);
            updateMsg = "Updating will start at: ";
            updateMsgTime = String.valueOf(getFirstTime().getTime());
        }
        else{
//             System.out.println("Automatically update is canceled");
             updateMsg= "Automatically update";
             updateMsgTime = "";
             timer.cancel();
             timer.purge();
        }
    }
    Calendar getFirstTime() {
        Calendar cal = Calendar.getInstance();

        int currentMinute = cal.get(Calendar.MINUTE);

        if (currentMinute < 45) {
            cal.set(Calendar.MINUTE, 45);
        }
        if (currentMinute < 30) {
            cal.set(Calendar.MINUTE, 30);
        }
        if (currentMinute < 15) {
            cal.set(Calendar.MINUTE, 15);
        }
        if (currentMinute >= 45) {
            cal.set(Calendar.HOUR_OF_DAY, cal.get(Calendar.HOUR_OF_DAY) + 1);
            cal.set(Calendar.MINUTE, 0);
        }
        cal.set(Calendar.SECOND, 0);
        return cal;
    }
    @RequestMapping(value = "/administrator/update", method = RequestMethod.GET)
    public String update() throws UnirestException, SQLException {
        updateServiceIF.runUpdate();
        return "redirect:/";
    }
}
