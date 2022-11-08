package com.orion.bitbucket.service.implementation;
import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.in;
import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import com.mongodb.client.*;
import com.orion.bitbucket.entity.pull_request.PRReviewersEntity;
import com.orion.bitbucket.entity.pull_request.common.PRUserEntity;
import com.orion.bitbucket.helper.DatabaseHelper;
import com.orion.bitbucket.helper.QueryHelper;
import com.orion.bitbucket.service.IDBQueryService;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.bson.conversions.Bson;
import com.mongodb.client.model.Projections;
import com.mongodb.client.model.Sorts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Data
@Slf4j // for logging
@NoArgsConstructor
@Component
public class DBQueryServiceImpl implements IDBQueryService {
    @Autowired
    MongoTemplate mongoTemplate;

    public void findPRSByEmail(String email, String DBCollectionName) {
        // Replace the uri string with your MongoDB deployment's connection string
        int counter=0;
        try (MongoClient mongoClient = MongoClients.create(DatabaseHelper.DATABASE_URL)) {
            log.debug("--> findPRSByEmail method started in DBQueryServiceImpl class.");
                MongoDatabase database = mongoClient.getDatabase(DatabaseHelper.DATABASE_NAME);
                MongoCollection<Document> collection = database.getCollection(DBCollectionName);
                BasicDBObject query = new BasicDBObject();
                query.put(QueryHelper.VALUES_REVIEWERS_USER_EMAIL_ADDRESS, email);
                Bson projectionFields = Projections.fields(
                        Projections.include(QueryHelper.VALUES_REVIEWERS_USER, QueryHelper.VALUES_TITLE));
                 MongoCursor<Document> cursor = collection.find(query)
                        .projection(projectionFields)
                        .sort(Sorts.descending()).iterator();


                try {

                    while(cursor.hasNext()) {
                        //System.out.println(cursor.next());
                        Document doc = cursor.next();
                        System.out.println(doc.toJson());
                        Document valuesTitle =(Document) doc.get("values");
                        System.out.println("-------------------------");
                        List<Document> reviewers = valuesTitle.getList("reviewers", Document.class);
                        for (int i = 0; i<reviewers.size(); i++){
                            Document user = (Document) reviewers.get(i).get("user");
                            System.out.println(user.getString("emailAddress"));
                        }
                       // System.out.println(reviewers);

                        counter +=1;
                    }
                }
                catch (Exception err){
                    System.out.println("there is an error in DBQuery service: " + err);
                }
                finally {
                cursor.close();

            }


        }
        catch (Exception err){
            log.error("There is a error in DBQueryServiceImpl. Error: {}", err);
            System.out.println("there is an error in DBQuery service: " + err);
        }
        finally {
            System.out.println("The user who has this '" + email + "' e-mail has maximum "+ counter + " PRs");

        }

    }
}

