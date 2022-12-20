package com.orion.bitbucket.repository;

import com.orion.bitbucket.entity.pull_request.PREntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PullRequestRepository extends MongoRepository<PREntity, String> {



}
