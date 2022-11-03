package com.orion.bitbucket.controllers;
import com.orion.bitbucket.services.IAllPRSService;
import com.orion.bitbucket.services.IProjectsService;
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
    private IAllPRSService allPRSService;
    @Autowired
    private IProjectsService projectsService;

    //Parameterized Constructor
    public PageController(IAllPRSService allPRSService, IProjectsService projectsService) {
        this.allPRSService = allPRSService;
        this.projectsService = projectsService;
    }

    // GETs METHODs

    // this method get all data from API and save them into MongoDB
    @GetMapping("/fill-data")
    public ResponseEntity<String> getAllData(){

        boolean boolProjects = projectsService.getAllProjects();
        boolean boolAllPRS = allPRSService.getAllPRS();

        if ( boolAllPRS && boolProjects){
            return ResponseEntity.status(HttpStatus.ACCEPTED).body("Successful.! Mission Complited :)");
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Sorry.! We could not success :(");

    }

    @GetMapping("get-prs")
    public void getSpecificPR(){
        Instant start = Instant.now();
        allPRSService.findAllPRWithEmail("can.eren@orioninc.com");
        Instant finish = Instant.now();
        Duration differenceTime = Duration.between(start,finish);
        System.out.println("Total duration of finding of user prs is: " + differenceTime.toSeconds() + " second.!");
    }


    // GETTERs and SETTERs

    public IAllPRSService getAllPRSService() {
        return allPRSService;
    }

    public void setAllPRSService(IAllPRSService allPRSService) {
        this.allPRSService = allPRSService;
    }

    public IProjectsService getProjectsService() {
        return projectsService;
    }

    public void setProjectsService(IProjectsService projectsService) {
        this.projectsService = projectsService;
    }


}
