package com.orion.bitbucket.service.implementation;
import com.google.gson.Gson;
import com.mongodb.BasicDBObject;
import com.mongodb.client.*;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.model.Updates;
import com.orion.bitbucket.config.EntityConfig;
import com.orion.bitbucket.config.UtilConfig;
import com.orion.bitbucket.entity.pull_request.PRAuthorEntity;
import com.orion.bitbucket.entity.pull_request.PREntity;
import com.orion.bitbucket.entity.pull_request.PRValuesEntity;
import com.orion.bitbucket.helper.DatabaseHelper;
import com.orion.bitbucket.helper.LogHelper;
import com.orion.bitbucket.helper.QueryHelper;
import com.orion.bitbucket.repository.PullRequestRepository;
import com.orion.bitbucket.service.IQueryService;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.springframework.data.mongodb.core.MongoTemplate;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.gt;
import org.bson.Document;
import org.bson.conversions.Bson;
import com.mongodb.MongoException;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Updates;
import com.mongodb.client.result.UpdateResult;

@Data
@Log4j2 // for logging
@NoArgsConstructor
@Component
public class QueryServiceImpl implements IQueryService {

    @Autowired
    private EntityConfig entityConfig;

    @Autowired
    private PullRequestRepository pullRequestRepository;
    @Autowired
    private UtilConfig utilConfig;

    @Autowired
    private MongoOperations mongoOperation;
    @Autowired
    private MongoTemplate mongoTemplate;

    public List<PREntity> getAllPullRequests(String query, String condition, String[] collectionNames) {
        List<PREntity> resutlAPI = new ArrayList<PREntity>();
        Gson gson = utilConfig.getGson();

        for (String collectionName : collectionNames){
            try (MongoClient mongoClient = MongoClients.create(DatabaseHelper.DATABASE_URL)) {
                if (LogHelper.IS_BASE_LOGGING){
                    log.info("getAllPullRequests method in QueryServiceImpl class was invoked for " + collectionName);
                }
                BasicDBObject basicQuery = getQuery(query, condition);

                MongoDatabase database = mongoClient.getDatabase(DatabaseHelper.DATABASE_NAME);
                MongoCollection<Document> collection = database.getCollection(collectionName);
                MongoCursor<Document> cursor = collection.find(basicQuery).iterator();
                while (cursor.hasNext()) {
                    PREntity entity = entityConfig.getPrototypePullRequestEntity(); // must create a new instance
                    Document doc = cursor.next();
                    Document values = (Document) doc.get("values");
                    JSONObject jsonObject = new JSONObject(doc.toJson());
                    entity.setId(jsonObject.getJSONObject("_id").getString("$oid"));
                    entity.setSize(jsonObject.getInt("size"));
                    entity.setLimit(jsonObject.getInt("limit"));
                    entity.setIsLastPage(jsonObject.getBoolean("isLastPage"));
                    entity.setStart(jsonObject.getInt("start"));
                    entity.setNextPageStart(jsonObject.getInt("nextPageStart"));
                    entity.setValues(gson.fromJson(values.toJson(), PRValuesEntity.class));
                    resutlAPI.add(entity);
                }


            } catch (Exception err) {
                if(LogHelper.IS_BASE_LOGGING){
                    log.error("There is a error in findPRSByEmail method in QueryServiceImpl class. Error: {}", err);
                }
            } finally {
                if (LogHelper.IS_BASE_LOGGING){
                    log.info("findPRSByEmail method in QueryServiseImpl class executing has finished");
                }
            }
        }
        return resutlAPI;
    }

    public BasicDBObject getQuery(String query, String condition){
        BasicDBObject basicQuery = new BasicDBObject();
        if(!query.isEmpty() && !condition.isEmpty()){
            if(condition.toLowerCase().equals("true")  || condition.toLowerCase().equals("false") ){
                boolean boolCondition= Boolean.parseBoolean(condition);
                basicQuery.put(query, boolCondition);
            }
            else {
                basicQuery.put(query, condition);

            }
        }
        return basicQuery;
    }

    @Override
    public String updateTeamNames(String[] userName, String teamNameText, String[] collectionNames) {
        System.out.println("UpdateNames çalışıyor");
        Gson gson = utilConfig.getGson();
        String resultText="";

        for (int i=0; i< userName.length; i++){
            System.out.println("For içi çalışıyor");
            for (String collectionName : collectionNames){
                try (MongoClient mongoClient = MongoClients.create(DatabaseHelper.DATABASE_URL)) {
                    if (LogHelper.IS_BASE_LOGGING) {
                        log.info("updateTeamNames method in QueryServiceImpl class was invoked for " + collectionName);
                    }
                    MongoDatabase database = mongoClient.getDatabase(DatabaseHelper.DATABASE_NAME);
                    MongoCollection<Document> collection = database.getCollection(collectionName);
                    BasicDBObject basicQuery = getQuery("values.author.user.name", userName[i]);
                    MongoCursor<Document> cursor = collection.find(basicQuery).iterator();
                    Bson query = eq("values.author.user.name", userName[i]);
                    Bson updates = Updates.set("values.author.teamName", teamNameText);
                        try {
                            UpdateResult result = collection.updateMany(query, updates);
                            resultText = "Updates are successful";
                        } catch (MongoException me) {
                            resultText = "Updates are unsuccessful." ;

                        }

                }
            }

        }
        return resultText;
    }
}

/*

 Document values = (Document) doc.get("values");
                        JSONObject jsonObject = new JSONObject(doc.toJson());
                        entity.setId(jsonObject.getJSONObject("_id").getString("$oid"));
                        entity.getValues().getAuthor().setTeamName(teamNameText);
                        entity.setValues(gson.fromJson(values.toJson(), PRValuesEntity.class));
                        resutlAPI.add(entity);
 */