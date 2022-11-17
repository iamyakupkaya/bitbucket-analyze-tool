package com.orion.bitbucket.service.implementation;
import com.google.gson.Gson;
import com.mongodb.BasicDBObject;
import com.mongodb.client.*;
import com.orion.bitbucket.config.EntityConfig;
import com.orion.bitbucket.config.UtilConfig;
import com.orion.bitbucket.entity.pull_request.PREntity;
import com.orion.bitbucket.entity.pull_request.PRValuesEntity;
import com.orion.bitbucket.helper.DatabaseHelper;
import com.orion.bitbucket.helper.LogHelper;
import com.orion.bitbucket.helper.QueryHelper;
import com.orion.bitbucket.service.IQueryService;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

@Data
@Log4j2 // for logging
@NoArgsConstructor
@Component
public class QueryServiceImpl implements IQueryService {

    @Autowired
    private EntityConfig entityConfig;
    @Autowired
    private UtilConfig utilConfig;
    public List<PREntity> findPRSByEmail(String email, String DBCollectionName) {
        List<PREntity> resutlAPI = new ArrayList<PREntity>();
        Gson gson = utilConfig.getGson();
        try (MongoClient mongoClient = MongoClients.create(DatabaseHelper.DATABASE_URL)) {
            if (LogHelper.IS_BASE_LOGGING){
                log.info("findPRSByEmail method in DBQueryServiceImpl class was invoked for {} email.", email);
            }
            MongoDatabase database = mongoClient.getDatabase(DatabaseHelper.DATABASE_NAME);
            MongoCollection<Document> collection = database.getCollection(DBCollectionName);
            BasicDBObject query = new BasicDBObject();
            query.put(QueryHelper.VALUES_REVIEWERS_USER_EMAIL_ADDRESS, email);
            MongoCursor<Document> cursor = collection.find(query).iterator();

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
                log.error("There is a error in findPRSByEmail method in DBQueryServiceImpl class. Error: {}", err);
            }
        } finally {
            if (LogHelper.IS_BASE_LOGGING){
                log.info("findPRSByEmail method in DBQueryServiseImpl class executing has finished");
            }
        }
    return resutlAPI;
    }
}

