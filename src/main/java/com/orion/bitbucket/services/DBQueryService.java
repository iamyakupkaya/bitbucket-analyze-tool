package com.orion.bitbucket.services;
import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.in;
import static com.mongodb.client.model.Projections.*;
import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

import com.mongodb.BasicDBObject;
import com.mongodb.client.*;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import org.bson.conversions.Bson;
import com.mongodb.client.model.Projections;
import com.mongodb.client.model.Sorts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

@Component
public class DBQueryService {
    @Autowired
    MongoTemplate mongoTemplate;

    public DBQueryService() {
    }

    public DBQueryService(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    public void findPRSByEmail(String email) {
        // Replace the uri string with your MongoDB deployment's connection string
        String uri = "mongodb://localhost:27017";
        int counter=0;
        try (MongoClient mongoClient = MongoClients.create(uri)) {
                MongoDatabase database = mongoClient.getDatabase("bitbucket");
                MongoCollection<Document> collection = database.getCollection("allPRS");
                BasicDBObject query = new BasicDBObject();
                query.put("values.reviewers.user.emailAddress", email);
                Bson projectionFields = Projections.fields(
                        Projections.include("values.reviewers.user.name", "values.reviewers.user.emailAddress", "values.reviewers.user.displayName", "values.title", "isLastPage", "start"));
                MongoCursor<Document> cursor = collection.find(query)
                        .projection(projectionFields)
                        .sort(Sorts.descending()).iterator();
                try {
                    while(cursor.hasNext()) {
                        System.out.println(cursor.next().toJson());
                        counter +=1;
                    }
                }  finally {
                cursor.close();

            }


        }
        finally {
            System.out.println("The user who has this '" + email + "' e-mail has maximum "+ counter + " PRs");

        }

    }
}

