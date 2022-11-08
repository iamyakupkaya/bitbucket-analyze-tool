package com.orion.bitbucket.repository.asrv;

import com.orion.bitbucket.entity.pull_request.asrv.AsRafCoreEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AsRafCoreRepository extends MongoRepository<AsRafCoreEntity, String> {

}
