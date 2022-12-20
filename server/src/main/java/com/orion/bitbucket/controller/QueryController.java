package com.orion.bitbucket.controller;

import com.google.gson.JsonObject;
import com.orion.bitbucket.entity.pull_request.PRAuthorEntity;
import com.orion.bitbucket.entity.pull_request.PREntity;
import com.orion.bitbucket.helper.ControllerHelper;
import com.orion.bitbucket.helper.DatabaseHelper;
import com.orion.bitbucket.helper.QueryHelper;
import com.orion.bitbucket.service.implementation.QueryServiceImpl;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
    @GetMapping(path = ControllerHelper.URL_GET_DATA_FROM_DB) // url --> /get-data
    public ResponseEntity<List<PREntity>> getAllPullRequests(@RequestParam(name = "query", required = false, defaultValue = "") String query, @RequestParam(name = "condition", required = false, defaultValue = "") String condition) {
        Instant start = Instant.now();
        List<PREntity> resultAPI = new ArrayList<PREntity>();
        resultAPI=queryService.getAllPullRequests(query, condition, DatabaseHelper.ALL_COLLECTIONS_ARRAY);
        Instant finish = Instant.now();
        Duration differenceTime = Duration.between(start, finish);
        System.out.println("Total duration of finding of user prs is: " + differenceTime.toMillis() + " millis.!");
        return ResponseEntity.status(HttpStatus.OK).body(resultAPI);
    }
    @CrossOrigin
    @PutMapping("/update-data/{teamText}")
    public String updateTeamName(@RequestBody String[] IDs, @PathVariable String teamText) {
        String result = queryService.updateTeamNames(IDs, teamText,DatabaseHelper.ALL_COLLECTIONS_ARRAY);
        for (String i : IDs) {
            System.out.println("gelen deper: "+ i);
        }
        return result;
    }

}
