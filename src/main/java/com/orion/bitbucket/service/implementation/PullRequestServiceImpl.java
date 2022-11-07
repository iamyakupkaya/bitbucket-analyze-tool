package com.orion.bitbucket.service.implementation;
import com.orion.bitbucket.config.EntityConfig;
import com.orion.bitbucket.helper.EndPointsHelper;
import com.orion.bitbucket.repository.AsrvMcpCoreRootRepository;
import com.orion.bitbucket.service.IPullRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

@Service
public class PullRequestServiceImpl implements IPullRequestService {
    @Autowired
    private AsrvMcpCoreRootRepository allPRSRepository;
    @Autowired
    private EntityConfig entityConfig;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private DBQueryServiceImpl queryService;

    @Autowired
    private CommonServiceImpl commonService;

    // CONSTRUCTOR


    public PullRequestServiceImpl(AsrvMcpCoreRootRepository allPRSRepository,
                                  EntityConfig entityConfig, MongoTemplate mongoTemplate,
                                  DBQueryServiceImpl queryService, CommonServiceImpl commonService) {
        this.allPRSRepository = allPRSRepository;
        this.entityConfig = entityConfig;
        this.mongoTemplate = mongoTemplate;
        this.queryService = queryService;
        this.commonService = commonService;
    }

    // this method get all prs and save to MongoDB
    public boolean getAllPRSData(String url){
       boolean result = commonService.getDataFromAPI(url, entityConfig.getAllPRSEntity());

       return result;
    }

    @Override
    public boolean getAllPRS() {
       boolean result = getAllPRSData(EndPointsHelper.ALL_PRS);
       return result;
    }

    // find one or more documents with user email if there is.
    @Override
    public void findAllPRWithEmail(String email) {
        queryService.findPRSByEmail(email);
    }




    // ***************** GETTERs and SETTERs *****************


    public AsrvMcpCoreRootRepository getAllPRSRepository() {
        return allPRSRepository;
    }

    public void setAllPRSRepository(AsrvMcpCoreRootRepository allPRSRepository) {
        this.allPRSRepository = allPRSRepository;
    }

    public EntityConfig getEntityConfig() {
        return entityConfig;
    }

    public void setEntityConfig(EntityConfig entityConfig) {
        this.entityConfig = entityConfig;
    }

    public MongoTemplate getMongoTemplate() {
        return mongoTemplate;
    }

    public void setMongoTemplate(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    public DBQueryServiceImpl getQueryService() {
        return queryService;
    }

    public void setQueryService(DBQueryServiceImpl queryService) {
        this.queryService = queryService;
    }

    public CommonServiceImpl getCommonService() {
        return commonService;
    }

    public void setCommonService(CommonServiceImpl commonService) {
        this.commonService = commonService;
    }
}
