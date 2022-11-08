package com.orion.bitbucket.controller;

import com.orion.bitbucket.helper.ControllerHelper;
import com.orion.bitbucket.helper.DatabaseHelper;
import com.orion.bitbucket.helper.EndPointsHelper;
import com.orion.bitbucket.helper.MessageHelper;
import com.orion.bitbucket.service.asrv.IMcpCoreRootService;
import com.orion.bitbucket.service.IPullRequestService;
import com.orion.bitbucket.service.IProjectsService;
import com.orion.bitbucket.service.asrv.implementation.AsRafCoreServiceImpl;
import com.orion.bitbucket.service.implementation.DBQueryServiceImpl;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.time.Instant;

@RestController
@Data
@Slf4j
@NoArgsConstructor
@RequestMapping(path = "/data")
public class PageController {
    //FIELDS
    @Autowired
    @Lazy
    private IMcpCoreRootService asrvMcpCoreRootService;

    @Autowired
    private DBQueryServiceImpl dbQueryService;
    @Autowired
    private IProjectsService projectsService;

    @Autowired
    private AsRafCoreServiceImpl asrvAsRafCoreService;

    // GETs METHODs

    // this method get all data from API and save them into MongoDB
    @GetMapping(ControllerHelper.URL_GET_All_DATA_FROM_API) // url --> /setup
    public ResponseEntity<String> getAllData() {

        boolean boolProjects = projectsService.getProjectsFromAPI(EndPointsHelper.BASE_URL);
        boolean boolAllPRS = asrvMcpCoreRootService.getAsrvMecpCoreRootPR(EndPointsHelper.ASRV_MCP_CORE_ROOT_URL);
        boolean boolAsRafCore=asrvAsRafCoreService.getAsrvAsRafCorePR(EndPointsHelper.ASRV_AS_RAF_CORE__URL);

        if(boolProjects){
            log.info("Project collection was created");
        }
        else {
            log.warn("project collection could not been created");
        }
        if (boolAllPRS && boolProjects && boolAsRafCore) {
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(MessageHelper.GET_ALL_DATA_SUCCESS_MESSAGE);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(MessageHelper.GET_ALL_DATA_FAILED_MESSAGE + "\nGetting projects is " +
                (boolProjects ? "successful.. " : "unsuccessful") + " and " + (boolAllPRS ? "successful.. " : "unsuccessful"));

    }


    @GetMapping(ControllerHelper.URL_GET_PRS_FROM_DB) // url --> /get-prs
    public void getSpecificPR() {
        Instant start = Instant.now();
        dbQueryService.findPRSByEmail("can.eren@orioninc.com",DatabaseHelper.PR_ASRV_MCP_CORE_ROOT);
        Instant finish = Instant.now();
        Duration differenceTime = Duration.between(start, finish);
        System.out.println("Total duration of finding of user prs is: " + differenceTime.toMillis() + " millis.!");
    }


}
