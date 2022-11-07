package com.orion.bitbucket.service.implementation;

import com.google.gson.Gson;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.orion.bitbucket.config.EntityConfig;
import com.orion.bitbucket.config.UtilConfig;
import com.orion.bitbucket.entity.ITopEntity;
import com.orion.bitbucket.entity.project.ProjectEntity;
import com.orion.bitbucket.entity.project.ProjectValuesEntity;
import com.orion.bitbucket.entity.pull_request.PREntity;
import com.orion.bitbucket.entity.pull_request.PRValuesEntity;
import com.orion.bitbucket.entity.pull_request.asrv.McpCoreRootEntity;
import com.orion.bitbucket.helper.EndPointsHelper;
import com.orion.bitbucket.helper.LogHelper;
import com.orion.bitbucket.log.Log;
import com.orion.bitbucket.repository.AsrvMcpCoreRootRepository;
import com.orion.bitbucket.repository.ProjectRepository;
import com.orion.bitbucket.service.IPullRequestService;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

@Service
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
    private AsrvMcpCoreRootRepository asrvMcpCoreRootRepository;

    @Autowired
    private JsonResponseServiceImpl response;

    // this method get all prs and save to MongoDB

    // find one or more documents with user email if there is.
    @Override
    public void findAllPRWithEmail(String email) {
        queryService.findPRSByEmail(email);
    }

    @Override
    public boolean getPullRequestFromAPI(String url, PREntity entity) throws JSONException {
        int start = 0;
        boolean isLastPage = false;
        try {
            Gson gson = utilConfig.getGson();
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
                    asrvMcpCoreRootRepository.save((McpCoreRootEntity) entity);
                    forStart = forStart + 1;
                }

                start = start + 100;
                isLastPage = body.getBoolean("isLastPage");
            }



            return true;

        } catch (Exception exception) {
            if (LogHelper.IS_BASE_LOGGING) {
                Log.logger(Log.LogConstant.TAG_WARN, String.valueOf(exception));
            }
            return false;
        }
    }




}

