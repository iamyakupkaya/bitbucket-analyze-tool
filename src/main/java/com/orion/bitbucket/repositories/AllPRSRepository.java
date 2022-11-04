package com.orion.bitbucket.repositories;

import com.orion.bitbucket.entities.PRSEntities.PRSEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AllPRSRepository extends MongoRepository<PRSEntity, String> {

}
