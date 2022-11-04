package com.orion.bitbucket.controller;

import com.orion.bitbucket.helper.ControllerHelper;
import com.orion.bitbucket.helper.MessageHelper;
import com.orion.bitbucket.service.IPullRequestService;
import com.orion.bitbucket.service.IProjectsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.time.Instant;

@RestController
public class PageController {
    //FIELDS
    @Autowired
    private IPullRequestService allPRSService;
    @Autowired
    private IProjectsService projectsService;

    //Parameterized Constructor
    public PageController(IPullRequestService allPRSService, IProjectsService projectsService) {
        this.allPRSService = allPRSService;
        this.projectsService = projectsService;
    }

    // GETs METHODs

    // this method get all data from API and save them into MongoDB
    @GetMapping(ControllerHelper.URL_GET_All_DATA_FROM_API) // url --> /setup
    public ResponseEntity<String> getAllData() {

        boolean boolProjects = projectsService.getAllProjects();
        boolean boolAllPRS = allPRSService.getAllPRS();

        if (boolAllPRS && boolProjects) {
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(MessageHelper.GET_ALL_DATA_SUCCESS_MESSAGE);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(MessageHelper.GET_ALL_DATA_FAILED_MESSAGE + "\nGetting projects is " +
                (boolProjects ? "successful.. " : "unsuccessful") + " and " + (boolAllPRS ? "successful.. " : "unsuccessful"));

    }


    @GetMapping(ControllerHelper.URL_GET_PRS_FROM_DB) // url --> /get-prs
    public void getSpecificPR() {
        Instant start = Instant.now();
        allPRSService.findAllPRWithEmail("yakup.kaya@orioninc.com");
        Instant finish = Instant.now();
        Duration differenceTime = Duration.between(start, finish);
        System.out.println("Total duration of finding of user prs is: " + differenceTime.toSeconds() + " second.!");
    }


}
