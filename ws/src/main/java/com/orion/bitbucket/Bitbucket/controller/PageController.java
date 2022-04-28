package com.orion.bitbucket.Bitbucket.controller;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.orion.bitbucket.Bitbucket.model.AuthorTotalPRs;
import com.orion.bitbucket.Bitbucket.service.PullRequestService;

@Controller
public class PageController {

   @Autowired
   private PullRequestService service;
   
   @RequestMapping(value = "/", method = RequestMethod.GET)
   public String edit(Model model) throws  UnirestException {
       service.getData(); // Bu metot uygulamanin baslangicinda cagirilmali ki listeler doldurulsun. Hangi kisim ilk calisacaksa bu orada cagirilmali.
      //List<PullRequest> pullrequests = service.reportAll();
      //model.addAttribute("pr", new PullRequest());
      //model.addAttribute("pullrequests", pullrequests);
      return "index.html";
   }

   @RequestMapping(value = "/test", method = RequestMethod.GET)
   public String test(Model model) throws  UnirestException {
      List<AuthorTotalPRs> getAllAuthor = service.getCountOfPrStatesOfAllAuthor();
      model.addAttribute("tst", new AuthorTotalPRs());
      model.addAttribute("getAllAuthor", getAllAuthor);
      return "index.html";
   }
   
   @RequestMapping(value ="/api/web-controller/test/@username={userName}")
   public String showAuthorDetails(Model model, @PathVariable(name = "userName", required = false) String userName) throws UnirestException{
	   // List<UserPrDetails> UserPrDetails = service.filterPRsByName(userName);
	   // model.addAttribute("prId", new UserPrDetails());
	   // model.addAttribute("UserPrDetails", UserPrDetails);
	   
	   
	   return "modalBox.html";
   }
   
   
  
}
