package com.orion.bitbucket.repositories;

import com.orion.bitbucket.entities.PRSEntities.AllPrsEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AllPRSRepository extends MongoRepository<AllPrsEntity, String> {

}
