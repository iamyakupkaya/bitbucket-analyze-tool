package com.orion.bitbucket.controller;

import com.google.gson.JsonObject;
import com.orion.bitbucket.entity.pull_request.PREntity;
import com.orion.bitbucket.helper.ControllerHelper;
import com.orion.bitbucket.helper.DatabaseHelper;
import com.orion.bitbucket.service.implementation.QueryServiceImpl;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Data
@Log4j2
@NoArgsConstructor
@RestController
@RequestMapping(path = "/api/v1")
public class QueryController {
    //FIELDS
    @Autowired
    private QueryServiceImpl queryService;

    // METHODS
    @CrossOrigin
    @GetMapping(path = ControllerHelper.URL_GET_PRS_FROM_DB) // url --> /get-prs
    public ResponseEntity<List<PREntity>> getSpecificPR(@PathParam("email") String email) {
        Instant start = Instant.now();
        List<PREntity> resultAPI = new ArrayList<PREntity>();
        resultAPI=queryService.findPRSByEmail(email, DatabaseHelper.COLLECTION_NAME_ASRV_MCP_CORE_ROOT);
        Instant finish = Instant.now();
        Duration differenceTime = Duration.between(start, finish);
        System.out.println("Total duration of finding of user prs is: " + differenceTime.toMillis() + " millis.!");
    return ResponseEntity.status(HttpStatus.OK).body(resultAPI);
    }
}
