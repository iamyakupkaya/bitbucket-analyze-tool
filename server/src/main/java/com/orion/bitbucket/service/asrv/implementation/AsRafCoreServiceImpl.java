package com.orion.bitbucket.service.asrv.implementation;

import com.orion.bitbucket.config.EntityConfig;
import com.orion.bitbucket.service.asrv.IAsRafCoreService;
import com.orion.bitbucket.service.implementation.PullRequestServiceImpl;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Data
@NoArgsConstructor
public class AsRafCoreServiceImpl implements IAsRafCoreService {
    @Autowired
    private PullRequestServiceImpl pullRequestService;
    @Autowired
    private EntityConfig entityConfig;

    @Override
    public boolean getAsrvAsRafCorePR(String url) {
        boolean result = pullRequestService.getPullRequestFromAPI(url, entityConfig.getAsRafCoreEntity());
        return result;
    }
}
