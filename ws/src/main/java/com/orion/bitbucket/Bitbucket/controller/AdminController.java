package com.orion.bitbucket.Bitbucket.controller;

import com.mashape.unirest.http.exceptions.UnirestException;
import com.orion.bitbucket.Bitbucket.model.UserDO;
import com.orion.bitbucket.Bitbucket.service.TeamServiceIF;
import com.orion.bitbucket.Bitbucket.service.UserServiceIF;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Controller
public class AdminController {
    @Autowired
    private UserServiceIF userServiceIF;
    @Autowired
    private TeamServiceIF teamServiceIF;

    @RequestMapping(value = "/administrator", method = RequestMethod.GET)
    public String administrator(Model model) throws UnirestException, SQLException {

        // userServiceIF.insertUserTable();  // Automatically pulls data from table PullRequest

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = null;
        if (principal instanceof UserDetails) {
            username = ((UserDetails)principal).getUsername();
        } else {
            username = principal.toString();
        }
        model.addAttribute("username",username);

        List<String> teams = teamServiceIF.getAllTeams();
        model.addAttribute("teams", teams);

        List<String> managers = teamServiceIF.getAllUsername();
        model.addAttribute("managers", managers);

        List<String> roles = userServiceIF.getRoles();
        model.addAttribute("roles", roles);


        ArrayList<UserDO> allUsers = userServiceIF.getAllUsers();
        model.addAttribute("allUser",new UserDO());
        model.addAttribute("allUsers", allUsers);

        return "admin-panel.html";
    }
    @RequestMapping(value = "administrator/team/insert", method = RequestMethod.GET)
    public String getTeamInsert(Model model, @RequestParam String teamCode ,@RequestParam String manager) throws UnirestException, SQLException {
        List<String> teams = teamServiceIF.getAllTeams();
        model.addAttribute("teams", teams);
        if(teams.size() == 0){
            teamServiceIF.insertTeams(teamCode,manager);
        }else{
            for(String team:teams){
                if(teamCode.contains(team)){
                    return "admin-panel.html";
                }
            }
            teamServiceIF.insertTeams(teamCode,manager);
        }
        return "admin-panel.html";
    }
    @RequestMapping(value = "administrator/updateTeamManager", method = RequestMethod.GET)
    public String getTeamManagerUpdate(Model model, @RequestParam String teamCode ,@RequestParam String manager) throws UnirestException, SQLException {
        ArrayList<UserDO> teamMembers = teamServiceIF.getTeamUsers(teamCode);
        for (int i = 0; i<teamMembers.size(); i++){
            String name = teamMembers.get(i).getUsername();
            if(manager.equals(name)){
                teamServiceIF.updateTeamManager(manager,teamCode);
            }
        }
        teamServiceIF.getTeamManagerAssign(teamCode,manager);
        return "admin-panel.html";
    }

    @RequestMapping(value = "administrator/team/delete", method = RequestMethod.GET)
    public String getTeamDelete(Model model, @RequestParam String teamCode) throws UnirestException, SQLException {
        teamServiceIF.deleteTeam(teamCode);
        return "admin-panel.html";
    }

    @RequestMapping(value = "administrator/user/add/", method = RequestMethod.GET)
    public String addUser(Model model, @RequestParam String username,@RequestParam String firstname,
                          @RequestParam String lastname, @RequestParam String password,
                          @RequestParam String email,   @RequestParam String teamCode,
                          @RequestParam String role) throws UnirestException, SQLException{
        userServiceIF.getCollectUserInformation(username,firstname,lastname,password,email,teamCode,role);
        return "admin-panel.html";
    }

    @RequestMapping(value = "administrator/user/teamEdit/", method = RequestMethod.GET)
    public String editTeamUser(Model model,@RequestParam String username,@RequestParam String teamCode) throws UnirestException, SQLException{
        teamServiceIF.updateTeamMembersWithUsername(username,teamCode);
        return "admin-panel.html";
    }

    @RequestMapping(value = "administrator/user/edit/", method = RequestMethod.GET)
    public String editUser(Model model, @RequestParam String username,@RequestParam String firstname,
                           @RequestParam String lastname, @RequestParam String password,
                           @RequestParam String email,   @RequestParam String teamCode,
                           @RequestParam String role, @RequestParam String oldUsername) throws UnirestException, SQLException{
        userServiceIF.getPreconditionForUpdate(username,firstname,lastname,password,email,teamCode,role,oldUsername);
        return "admin-panel.html";
    }
    @RequestMapping(value = "administrator/user/delete/", method = RequestMethod.GET)
    public String deleteUser(Model model,  @RequestParam String username) throws UnirestException, SQLException {
        userServiceIF.getDeleteUserWithUserName(username);
        return "admin-panel.html";
    }
}
