package com.orion.bitbucket.service.implementation;
import com.orion.bitbucket.config.EntityConfig;
import com.orion.bitbucket.config.UtilConfig;
import com.orion.bitbucket.helper.EndPointsHelper;
import com.orion.bitbucket.repository.ProjectRepository;
import com.orion.bitbucket.service.IProjectsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProjectsServiceImpl implements IProjectsService {
    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private EntityConfig entityConfig;
    @Autowired
    private UtilConfig utilConfig;

    @Autowired
    CommonServiceImpl commonService;


    // Constructor
    public ProjectsServiceImpl(ProjectRepository projectRepository, CommonServiceImpl commonService, EntityConfig entityConfig, UtilConfig utilConfig) {
        this.projectRepository = projectRepository;
        this.entityConfig = entityConfig;
        this.utilConfig = utilConfig;
        this.commonService=commonService;
    }

    @Override
    public boolean getAllProjects() {
        boolean result = getProjects(EndPointsHelper.BASE_URL);
        return result;
    }

    private boolean getProjects(String url) {
        String urlAdd="?limit=100&start=";
        boolean result = commonService.getDataFromAPI(url+urlAdd, entityConfig.getProjectsEntity());
        return result;
    }
// GETTERs and SETTERs


    public ProjectRepository getProjectRepository() {
        return projectRepository;
    }

    public void setProjectRepository(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    public EntityConfig getEntityConfig() {
        return entityConfig;
    }

    public void setEntityConfig(EntityConfig entityConfig) {
        this.entityConfig = entityConfig;
    }

    public UtilConfig getUtilConfig() {
        return utilConfig;
    }

    public void setUtilConfig(UtilConfig utilConfig) {
        this.utilConfig = utilConfig;
    }


}

/*


    int start = 0;

        boolean isLastPage = false;
        try{
            // create a projectsEntity on EntityConfig.

            ProjectEntity projectsEntity =(ProjectEntity) entityConfig.getProjectsEntity();
            Gson gson=utilConfig.getGson();
            while (!isLastPage) {
                //get json with httpResponse.
                HttpResponse<JsonNode> httpResponse = response.getResponse(url+ urlAdd + start, EndPointsHelper.Bearer.TOKEN);
                JSONObject body = httpResponse.getBody().getObject(); // JSONObject
                int forStart=start;
                // this cycle take a response which has 100 values, and break into pieces for all value per.
                for(int i=0; i<body.getInt("size"); i++){
                    projectsEntity.setSize(1);
                    projectsEntity.setLimit(1);
                    projectsEntity.setStart(forStart);
                    // if just work for last value
                    if(body.getInt("size")<100 && (i+1 == body.getJSONArray("values").length())){
                        projectsEntity.setIsLastPage(true);
                        projectsEntity.setNextPageStart(-1);

                    }else {
                        projectsEntity.setIsLastPage(false);
                        projectsEntity.setNextPageStart(forStart+1);
                    }

                    ValuesEntity jsonToValuesEntity = gson.fromJson(body.getJSONArray("values").get(i).toString(), ValuesEntity.class);
                    projectsEntity.setValues(jsonToValuesEntity);
                    projectRepository.save(projectsEntity); // save to MongoDB
                    forStart = forStart +1;
                }
                start = start +100;
                isLastPage =body.getBoolean("isLastPage");

            }


            return true;


        }catch (Exception exception){
            if(IS_BASE_LOGGING){
                Log.logger(Log.LogConstant.TAG_WARN,String.valueOf(exception));}

            return false;

        }
 */
