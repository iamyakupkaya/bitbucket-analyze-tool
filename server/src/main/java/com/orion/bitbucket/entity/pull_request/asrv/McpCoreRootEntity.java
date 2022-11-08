package com.orion.bitbucket.entity.pull_request.asrv;

import com.orion.bitbucket.entity.pull_request.PREntity;
import com.orion.bitbucket.entity.pull_request.PRValuesEntity;
import com.orion.bitbucket.helper.DatabaseHelper;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(DatabaseHelper.PR_ASRV_MCP_CORE_ROOT) // PR_ASRV_mcp_core_root
public class McpCoreRootEntity extends PREntity {
    public McpCoreRootEntity() {
    }

    public McpCoreRootEntity(int size, int limit, boolean isLastPage, int start, int nextPageStart, PRValuesEntity values) {
        super(size, limit, isLastPage, start, nextPageStart, values);
    }
}
