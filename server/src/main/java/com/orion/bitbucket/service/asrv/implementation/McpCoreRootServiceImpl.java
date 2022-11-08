package com.orion.bitbucket.service.asrv.implementation;

import com.orion.bitbucket.config.EntityConfig;
import com.orion.bitbucket.service.asrv.IMcpCoreRootService;
import com.orion.bitbucket.service.implementation.PullRequestServiceImpl;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Data
@NoArgsConstructor
public class McpCoreRootServiceImpl implements IMcpCoreRootService {

    @Autowired
    PullRequestServiceImpl pullRequestService;

    @Autowired
    private EntityConfig entityConfig;

    @Override
    public boolean getAsrvMcpCoreRootPR(String url) {
        boolean result = pullRequestService.getPullRequestFromAPI(url, entityConfig.getMcpCoreRootEntity());
        return result;
    }
}
