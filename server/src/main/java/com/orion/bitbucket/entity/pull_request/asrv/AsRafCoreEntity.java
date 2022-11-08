package com.orion.bitbucket.entity.pull_request.asrv;

import com.orion.bitbucket.entity.pull_request.PREntity;
import com.orion.bitbucket.entity.pull_request.PRValuesEntity;
import com.orion.bitbucket.helper.DatabaseHelper;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(DatabaseHelper.PR_ASRV_AS_RAF_CORE) //  "PR_ASRV_as_raf_core"
public class AsRafCoreEntity extends PREntity {

    public AsRafCoreEntity() {
    }

    public AsRafCoreEntity(int size, int limit, boolean isLastPage, int start, int nextPageStart, PRValuesEntity values) {
        super(size, limit, isLastPage, start, nextPageStart, values);
    }
}
