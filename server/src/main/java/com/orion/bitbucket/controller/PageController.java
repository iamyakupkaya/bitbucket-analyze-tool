package com.orion.bitbucket.controller;
import com.orion.bitbucket.config.EntityConfig;
import com.orion.bitbucket.helper.*;
import com.orion.bitbucket.service.IPullRequestService;
import com.orion.bitbucket.service.IProjectsService;
import com.orion.bitbucket.service.implementation.QueryServiceImpl;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.Duration;
import java.time.Instant;

@RestController
@Data
@Log4j2
@NoArgsConstructor
@RequestMapping(path = "/data")
public class PageController {
    //FIELDS
    @Autowired
    private QueryServiceImpl dbQueryService;
    @Autowired
    private IProjectsService projectsService;

    @Autowired
    private IPullRequestService pullRequestService;

    @Autowired
    private EntityConfig entityConfig;

    // GETs METHODs

    // this method get all data from API and save them into MongoDB
    @GetMapping(ControllerHelper.URL_GET_All_DATA_FROM_API) // url --> /setup
    public ResponseEntity<String> getAllData() {
        try {
            if (LogHelper.IS_BASE_LOGGING){
                log.info("Bitbucket project is starting to fill data on data/setup url.");

            }
            // for All PROJECTS
            boolean boolProjects = projectsService.getProjectsFromAPI(EndPointsHelper.BASE_URL);
            if(boolProjects && LogHelper.IS_BASE_LOGGING){
                log.info(DatabaseHelper.PROJECTS + " collection was created in bitbucket database.");
            }
            else {
                if(LogHelper.IS_BASE_LOGGING){
                    log.warn( DatabaseHelper.PROJECTS + " collection could not been created in bitbucket database.");

                }
            }
            // for ASVR Project MCP_CORE_ROOT repos PRs
            boolean boolAllPRS = pullRequestService.getPullRequestFromAPI(EndPointsHelper.ASRV_MCP_CORE_ROOT_URL,
                    DatabaseHelper.COLLECTION_NAME_ASRV_MCP_CORE_ROOT,
                    entityConfig.getPullRequestEntity());
            if(boolProjects && LogHelper.IS_BASE_LOGGING){
                    log.info(DatabaseHelper.COLLECTION_NAME_ASRV_MCP_CORE_ROOT + " collection was created in bitbucket database.");
            }
            else {
                if(LogHelper.IS_BASE_LOGGING){
                    log.warn(DatabaseHelper.COLLECTION_NAME_ASRV_MCP_CORE_ROOT + " collection could not been created in bitbucket database.");
                }
            }
            // for ASVR Project AS_RAF_CORE repos PRs
            boolean boolAsRafCore=pullRequestService.getPullRequestFromAPI(EndPointsHelper.ASRV_AS_RAF_CORE_URL,
                    DatabaseHelper.COLLECTION_NAME_ASRV_AS_RAF_CORE, entityConfig.getPullRequestEntity());
            if(boolAsRafCore && LogHelper.IS_BASE_LOGGING){
                log.info(DatabaseHelper.COLLECTION_NAME_ASRV_AS_RAF_CORE + " collection was created in bitbucket database.");
            }
            else {
                if(LogHelper.IS_BASE_LOGGING){
                    log.warn(DatabaseHelper.COLLECTION_NAME_ASRV_AS_RAF_CORE +  " collection could not been created in bitbucket database.");
                }
            }

            // for IAC Project iac repos PRs
            boolean boolIacIac=pullRequestService.getPullRequestFromAPI(EndPointsHelper.IAC_IAC_URL,
                    DatabaseHelper.COLLECTION_NAME_IAC_IAC, entityConfig.getPullRequestEntity());
            if(boolIacIac && LogHelper.IS_BASE_LOGGING){
                log.info(DatabaseHelper.COLLECTION_NAME_IAC_IAC + " collection was created in bitbucket database.");
            }
            else {
                if(LogHelper.IS_BASE_LOGGING){
                    log.warn(DatabaseHelper.COLLECTION_NAME_IAC_IAC +  " collection could not been created in bitbucket database.");
                }
            }

            if (boolAllPRS && boolProjects && boolAsRafCore) {
                return ResponseEntity.status(HttpStatus.ACCEPTED).body(MessageHelper.GET_ALL_DATA_SUCCESS_MESSAGE);
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(MessageHelper.GET_ALL_DATA_FAILED_MESSAGE);


        }
        catch (Exception err){
            log.error("There is an error in getAllData method in PageController class. Error: {}", err);
        }
        finally {
            log.info("getAllData method in PageController class executing has finished");
        }

        return null;
    }

}
