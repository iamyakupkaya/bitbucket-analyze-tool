package com.orion.bitbucket.repository.asrv;
import com.orion.bitbucket.entity.pull_request.asrv.McpCoreRootEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface McpCoreRootRepository extends MongoRepository<McpCoreRootEntity, String> {

}