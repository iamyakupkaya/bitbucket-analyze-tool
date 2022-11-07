package com.orion.bitbucket.service.implementation;

import com.orion.bitbucket.config.EntityConfig;
import com.orion.bitbucket.service.IAsrvMcpCoreRootService;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Data
@NoArgsConstructor
public class AsrvMcpCoreRootServiceImpl  implements IAsrvMcpCoreRootService {

    @Autowired
    PullRequestServiceImpl pullRequestService;

    @Autowired
    private EntityConfig entityConfig;

    @Override
    public boolean getAsrvMecpCoreRootPR(String url) {
        boolean result = pullRequestService.getPullRequestFromAPI(url, entityConfig.getAllPrEntity());
        return result;
    }
}
