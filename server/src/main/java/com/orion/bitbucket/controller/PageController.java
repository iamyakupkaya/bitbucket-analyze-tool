package com.orion.bitbucket.controller;
import com.orion.bitbucket.config.EntityConfig;
import com.orion.bitbucket.helper.*;
import com.orion.bitbucket.service.IPullRequestService;
import com.orion.bitbucket.service.IProjectService;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Data
@Log4j2
@NoArgsConstructor
@RequestMapping(path = "/api/v1")
public class PageController {
    @Autowired
    private IProjectService projectsService;
    @Autowired
    private IPullRequestService pullRequestService;
    @Autowired
    private EntityConfig entityConfig;

    // this method get all data from API and save them into MongoDB
    @CrossOrigin
    @GetMapping(ControllerHelper.URL_GET_All_DATA_FROM_API) // url --> /setup
    public ResponseEntity<String> getAllData() {
        try {
            if (LogHelper.IS_BASE_LOGGING){
                log.info(MessageHelper.PAGE_CONTROLLER_GET_ALL_DATA_INVOKED_INFO_MESSAGE);

            }
            // for All PROJECTS
            boolean boolProjects = projectsService.getProjectsFromAPI(EndPointsHelper.BASE_URL);
            if(boolProjects && LogHelper.IS_BASE_LOGGING){
                log.info(MessageHelper.PAGE_CONTROLLER_GET_ALL_DATA_COMMON_INFO_MESSAGE + DatabaseHelper.PROJECTS);
            }
            else {
                if(LogHelper.IS_BASE_LOGGING){
                    log.warn(MessageHelper.PAGE_CONTROLLER_GET_ALL_DATA_COMMON_WARN_MESSAGE + DatabaseHelper.PROJECTS);

                }
            }
            // for ASVR Project MCP_CORE_ROOT repos PRs
            boolean boolAllPRS = pullRequestService.getPullRequestFromAPI(EndPointsHelper.ASRV_MCP_CORE_ROOT_URL,
                    DatabaseHelper.COLLECTION_NAME_ASRV_MCP_CORE_ROOT,
                    entityConfig.getPullRequestEntity());
            if(boolProjects && LogHelper.IS_BASE_LOGGING){
                    log.info(MessageHelper.PAGE_CONTROLLER_GET_ALL_DATA_COMMON_INFO_MESSAGE + DatabaseHelper.COLLECTION_NAME_ASRV_MCP_CORE_ROOT);
            }
            else {
                if(LogHelper.IS_BASE_LOGGING){
                    log.warn(MessageHelper.PAGE_CONTROLLER_GET_ALL_DATA_COMMON_WARN_MESSAGE + DatabaseHelper.COLLECTION_NAME_ASRV_MCP_CORE_ROOT);
                }
            }
            // for ASVR Project AS_RAF_CORE repos PRs
            boolean boolAsRafCore=pullRequestService.getPullRequestFromAPI(EndPointsHelper.ASRV_AS_RAF_CORE_URL,
                    DatabaseHelper.COLLECTION_NAME_ASRV_AS_RAF_CORE, entityConfig.getPullRequestEntity());
            if(boolAsRafCore && LogHelper.IS_BASE_LOGGING){
                log.info(MessageHelper.PAGE_CONTROLLER_GET_ALL_DATA_COMMON_INFO_MESSAGE + DatabaseHelper.COLLECTION_NAME_ASRV_AS_RAF_CORE);
            }
            else {
                if(LogHelper.IS_BASE_LOGGING){
                    log.warn(MessageHelper.PAGE_CONTROLLER_GET_ALL_DATA_COMMON_WARN_MESSAGE + DatabaseHelper.COLLECTION_NAME_ASRV_AS_RAF_CORE);
                }
            }

            // for IAC Project iac repos PRs
            boolean boolIacIac=pullRequestService.getPullRequestFromAPI(EndPointsHelper.IAC_IAC_URL,
                    DatabaseHelper.COLLECTION_NAME_IAC_IAC, entityConfig.getPullRequestEntity());
            if(boolIacIac && LogHelper.IS_BASE_LOGGING){
                log.info(MessageHelper.PAGE_CONTROLLER_GET_ALL_DATA_COMMON_INFO_MESSAGE + DatabaseHelper.COLLECTION_NAME_IAC_IAC);
            }
            else {
                if(LogHelper.IS_BASE_LOGGING){
                    log.warn(MessageHelper.PAGE_CONTROLLER_GET_ALL_DATA_COMMON_WARN_MESSAGE + DatabaseHelper.COLLECTION_NAME_IAC_IAC);
                }
            }

            if (boolAllPRS && boolProjects && boolAsRafCore) {
                return ResponseEntity.status(HttpStatus.ACCEPTED).body(MessageHelper.GET_ALL_DATA_SUCCESS_MESSAGE);
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(MessageHelper.GET_ALL_DATA_FAILED_MESSAGE);


        }
        catch (Exception err){
            log.error(MessageHelper.PAGE_CONTROLLER_GET_ALL_DATA_ERROR_MESSAGE, err);
        }
        finally {
            log.info(MessageHelper.PAGE_CONTROLLER_GET_ALL_DATA_FINALLY_INFO_MESSAGE);
        }

        return null;
    }

}
