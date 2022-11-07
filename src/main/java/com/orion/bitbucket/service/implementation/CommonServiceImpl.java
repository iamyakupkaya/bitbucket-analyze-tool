package com.orion.bitbucket.service.implementation;

import com.google.gson.Gson;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.orion.bitbucket.config.UtilConfig;
import com.orion.bitbucket.entity.ITopEntity;
import com.orion.bitbucket.entity.pull_request.PREntity;
import com.orion.bitbucket.entity.pull_request.PRValuesEntity;
import com.orion.bitbucket.entity.project.ProjectEntity;
import com.orion.bitbucket.entity.project.ProjectValuesEntity;
import com.orion.bitbucket.entity.pull_request.asrv.McpCoreRootEntity;
import com.orion.bitbucket.helper.EndPointsHelper;
import com.orion.bitbucket.log.Log;
import com.orion.bitbucket.repository.AsrvMcpCoreRootRepository;
import com.orion.bitbucket.repository.ProjectRepository;
import com.orion.bitbucket.service.ICommonService;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommonServiceImpl implements ICommonService {
    @Autowired
    private UtilConfig utilConfig;

    @Autowired
    private AsrvMcpCoreRootRepository allPRSRepository;

    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private JsonResponseServiceImpl response;


    private final boolean IS_BASE_LOGGING = false;

    //constructor


    public CommonServiceImpl(UtilConfig utilConfig, AsrvMcpCoreRootRepository allPRSRepository,
                             ProjectRepository projectRepository, JsonResponseServiceImpl response) {
        this.utilConfig = utilConfig;
        this.allPRSRepository = allPRSRepository;
        this.projectRepository = projectRepository;
        this.response = response;
    }

    @Override
    public boolean getDataFromAPI(String url, ITopEntity entity) throws JSONException {
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
                    if (entity instanceof McpCoreRootEntity) {
                        PRValuesEntity jsonToValuesEntity = gson.fromJson(body.getJSONArray("values").get(i).toString(), PRValuesEntity.class);
                        ((McpCoreRootEntity) entity).setValues(jsonToValuesEntity);
                        allPRSRepository.save((McpCoreRootEntity) entity);
                    } else if (entity instanceof ProjectEntity) {
                        ProjectValuesEntity jsonToValuesEntity = gson.fromJson(body.getJSONArray("values").get(i).toString(), ProjectValuesEntity.class);
                        ((ProjectEntity) entity).setValues(jsonToValuesEntity);
                        projectRepository.save((ProjectEntity) entity);

                    }
                    forStart = forStart + 1;
                }
                start = start + 100;
                isLastPage = body.getBoolean("isLastPage");

            }
            return true;

        } catch (Exception exception) {
            if (IS_BASE_LOGGING) {
                Log.logger(Log.LogConstant.TAG_WARN, String.valueOf(exception));
            }
            return false;
        }
    }


    // GETTERs and SETTERS


    public UtilConfig getUtilConfig() {
        return utilConfig;
    }

    public void setUtilConfig(UtilConfig utilConfig) {
        this.utilConfig = utilConfig;
    }

    public AsrvMcpCoreRootRepository getAllPRSRepository() {
        return allPRSRepository;
    }

    public void setAllPRSRepository(AsrvMcpCoreRootRepository allPRSRepository) {
        this.allPRSRepository = allPRSRepository;
    }

    public ProjectRepository getProjectRepository() {
        return projectRepository;
    }

    public void setProjectRepository(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    public JsonResponseServiceImpl getResponse() {
        return response;
    }

    public void setResponse(JsonResponseServiceImpl response) {
        this.response = response;
    }

    public boolean isIS_BASE_LOGGING() {
        return IS_BASE_LOGGING;
    }
}
