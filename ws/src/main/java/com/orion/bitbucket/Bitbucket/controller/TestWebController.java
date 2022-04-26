package com.orion.bitbucket.Bitbucket.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import com.google.gson.JsonSyntaxException;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.orion.bitbucket.Bitbucket.model.PullRequest;
import com.orion.bitbucket.Bitbucket.model.UserPrDetails;
import com.orion.bitbucket.Bitbucket.service.PullRequestService;

@Controller
public class TestWebController {

   @Autowired
   private PullRequestService service;
   
   @RequestMapping(value = "/api/web-controller/test", method = RequestMethod.GET)
   public String edit(Model model) throws JsonSyntaxException, UnirestException {
       service.getData(); // Bu metot uygulamanin baslangicinda cagirilmali ki listeler doldurulsun. Hangi kisim ilk calisacaksa bu orada cagirilmali.
      //List<PullRequest> pullrequests = service.reportAll();
      //model.addAttribute("pr", new PullRequest());
      //model.addAttribute("pullrequests", pullrequests);
      return "index.html";
   }
   
   @RequestMapping(value ="/api/web-controller/test/@username={userName}")
   public String showAuthorDetails(Model model, @PathVariable(name = "userName", required = false) String userName) throws JsonSyntaxException, UnirestException{
	   List<UserPrDetails> UserPrDetails = service.filterPRsByName(userName);
	   model.addAttribute("prId", new UserPrDetails());
	   model.addAttribute("UserPrDetails", UserPrDetails);
	   
	   
	   return "modalBox.html";
   }
   
   @RequestMapping(value ="/testt")
   public String test1() throws JsonSyntaxException, UnirestException{
	
	
	   
	   
	   return "reviewersStatistics.html";
   }
   
   
  
}
