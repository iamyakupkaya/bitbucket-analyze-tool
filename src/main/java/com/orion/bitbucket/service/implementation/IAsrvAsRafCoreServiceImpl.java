package com.orion.bitbucket.service.implementation;

import com.orion.bitbucket.config.EntityConfig;
import com.orion.bitbucket.service.IAsrvAsRafCoreService;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Data
@NoArgsConstructor
public class IAsrvAsRafCoreServiceImpl implements IAsrvAsRafCoreService {
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
