package com.orion.bitbucket.Bitbucket.controller;

import com.orion.bitbucket.Bitbucket.service.AuthorServiceIF;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class AuthorController {

    @Autowired
    private AuthorServiceIF authorServiceIF;

    @RequestMapping(value = "/author-page", method = RequestMethod.GET)
    public String authorMain(Model model) {
        int count = authorServiceIF.getAuthorCount();
        return "index.html";
    }
}
