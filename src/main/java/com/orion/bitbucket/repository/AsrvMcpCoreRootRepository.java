package com.orion.bitbucket.repository;

import com.orion.bitbucket.entity.pull_request.PREntity;
import com.orion.bitbucket.entity.pull_request.asrv.McpCoreRootEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AsrvMcpCoreRootRepository extends MongoRepository<McpCoreRootEntity, String> {

}
