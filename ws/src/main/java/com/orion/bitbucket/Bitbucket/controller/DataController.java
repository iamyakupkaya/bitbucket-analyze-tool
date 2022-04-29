package com.orion.bitbucket.Bitbucket.controller;

import com.mashape.unirest.http.exceptions.UnirestException;
import com.orion.bitbucket.Bitbucket.service.BaseServiceIF;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class DataController {

    @Autowired
    private BaseServiceIF baseService;

    @RequestMapping(value = "/setup", method = RequestMethod.GET)
    public String getData() {
        baseService.getData();
        return "index.html";
    }
}
