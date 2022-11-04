package com.orion.bitbucket.repository;

import com.orion.bitbucket.entity.pull_request.PREntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PRSRepository extends MongoRepository<PREntity, String> {

}
