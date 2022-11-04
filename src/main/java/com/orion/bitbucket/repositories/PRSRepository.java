package com.orion.bitbucket.repositories;

import com.orion.bitbucket.entities.PREntities.PREntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PRSRepository extends MongoRepository<PREntity, String> {

}
