package com.orion.bitbucket.Bitbucket.controller;

import com.orion.bitbucket.Bitbucket.model.AuthorDO;
import com.orion.bitbucket.Bitbucket.service.AuthorServiceIF;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.ArrayList;

@Controller
public class AuthorController {

    @Autowired
    private AuthorServiceIF authorServiceIF;

    @RequestMapping(value = "/author", method = RequestMethod.GET)
    public String authorMain(Model model) {
        int authorCount = authorServiceIF.getAuthorCount();
        ArrayList<AuthorDO> authors = authorServiceIF.getCountOfPrStatesOfAllAuthor();
        model.addAttribute("author", new AuthorDO());
        model.addAttribute("authors", authors);
        model.addAttribute("authorCount", authorCount);
        return "author.html";
    }
}
