package com.orion.bitbucket.service.implementation;
import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.in;
import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

import com.mongodb.BasicDBObject;
import com.mongodb.client.*;
import com.orion.bitbucket.helper.DatabaseHelper;
import com.orion.bitbucket.helper.QueryHelper;
import com.orion.bitbucket.service.IDBQueryService;
import org.bson.Document;
import org.bson.conversions.Bson;
import com.mongodb.client.model.Projections;
import com.mongodb.client.model.Sorts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

@Component
public class DBQueryServiceImpl implements IDBQueryService {
    @Autowired
    MongoTemplate mongoTemplate;

    public DBQueryServiceImpl() {
    }

    public DBQueryServiceImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    public void findPRSByEmail(String email) {
        // Replace the uri string with your MongoDB deployment's connection string
        int counter=0;
        try (MongoClient mongoClient = MongoClients.create(DatabaseHelper.DATABASE_URL)) {
                MongoDatabase database = mongoClient.getDatabase(DatabaseHelper.DATABASE_NAME);
                MongoCollection<Document> collection = database.getCollection(DatabaseHelper.COLLECTION_ALL_PRS);
                BasicDBObject query = new BasicDBObject();
                query.put(QueryHelper.VALUES_REVIEWERS_USER_EMAIL_ADDRESS, email);
                Bson projectionFields = Projections.fields(
                        Projections.include(QueryHelper.VALUES_REVIEWERS_USER, QueryHelper.VALUES_TITLE));
                MongoCursor<Document> cursor = collection.find(query)
                        .projection(projectionFields)
                        .sort(Sorts.descending()).iterator();
                try {
                    while(cursor.hasNext()) {
                        System.out.println(cursor.next().toJson());
                        counter +=1;
                    }
                }
                catch (Exception err){
                    System.out.println("there is an error: " + err);
                }
                finally {
                cursor.close();

            }


        }
        catch (Exception err){
            System.out.println("there is an error: " + err);
        }
        finally {
            System.out.println("The user who has this '" + email + "' e-mail has maximum "+ counter + " PRs");

        }

    }
}

