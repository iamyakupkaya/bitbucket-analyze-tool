package com.orion.bitbucket.service.implementation;

import com.google.gson.Gson;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.orion.bitbucket.config.UtilConfig;
import com.orion.bitbucket.entity.pull_request.PREntity;
import com.orion.bitbucket.entity.pull_request.PRValuesEntity;
import com.orion.bitbucket.entity.pull_request.asrv.AsRafCoreEntity;
import com.orion.bitbucket.entity.pull_request.asrv.McpCoreRootEntity;
import com.orion.bitbucket.helper.DatabaseHelper;
import com.orion.bitbucket.helper.EndPointsHelper;
import com.orion.bitbucket.helper.LogHelper;
import com.orion.bitbucket.repository.asrv.AsRafCoreRepository;
import com.orion.bitbucket.repository.asrv.McpCoreRootRepository;
import com.orion.bitbucket.service.IPullRequestService;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@Data // setter, getter, toString, constructorWithArgs, HashCode.. together
@NoArgsConstructor // default constructor
public class PullRequestServiceImpl implements IPullRequestService {


    @Autowired
    private DBQueryServiceImpl queryService;

    @Autowired
    @Lazy
    private PullRequestServiceImpl pullRequestService;

    @Autowired
    private UtilConfig utilConfig;

    @Autowired
    @Lazy
    private McpCoreRootRepository asrvMcpCoreRootRepository;

    @Autowired
    private AsRafCoreRepository asRafCoreRepository;

    @Autowired
    private JsonResponseServiceImpl response;

    // this method get all prs and save to MongoDB

    // find one or more documents with user email if there is.

    @Override
    public boolean getPullRequestFromAPI(String url, PREntity entity) throws JSONException {
        int start = 0;
        boolean isLastPage = false;
        try {
            Gson gson = utilConfig.getGson();
            if (LogHelper.IS_BASE_LOGGING){
                log.info("getPullRequestFromAPI method in PullRequestServiseImpl class was invoked.");
            }
            while (!isLastPage) {
                HttpResponse<JsonNode> httpResponse = response.getResponse(url + start, EndPointsHelper.Bearer.TOKEN);
                JSONObject body = httpResponse.getBody().getObject(); // JSONObject

                int forStart = start;
                // this cycle take a response which has 100 values, and break into pieces for all value per.
                for (int i = 0; i < body.getInt("size"); i++) {
                    entity.setSize(1);
                    entity.setLimit(1);
                    entity.setStart(forStart);
                    // if just work for last value
                    if (body.getInt("size") < 100 && (i + 1 == body.getJSONArray("values").length())) {
                        entity.setIsLastPage(true);
                        entity.setNextPageStart(-1);
                    } else {
                        entity.setIsLastPage(false);
                        entity.setNextPageStart(forStart + 1);
                    }
                    // checking which entity
                    PRValuesEntity jsonToValuesEntity = gson.fromJson(body.getJSONArray("values").get(i).toString(), PRValuesEntity.class);
                    entity.setValues(jsonToValuesEntity);
                    if(entity instanceof McpCoreRootEntity){
                        asrvMcpCoreRootRepository.save((McpCoreRootEntity) entity);
                    } else if (entity instanceof AsRafCoreEntity) {
                        asRafCoreRepository.save((AsRafCoreEntity) entity);
                    }
                    forStart = forStart + 1;
                }

                start = start + 100;
                isLastPage = body.getBoolean("isLastPage");
            }



            return true;

        } catch (Exception err) {
            if (LogHelper.IS_BASE_LOGGING) {
                log.warn("There is an error in getPullRequestFromAPI method in PullRequestServiseImpl class. Error: {}", err);
            }
            return false;
        }
        finally {
            if (LogHelper.IS_BASE_LOGGING){
                log.info("getPullRequestFromAPI method in PullRequestServiseImpl class executing has finished");

            }
        }
    }




}

