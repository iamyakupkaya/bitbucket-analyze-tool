package com.orion.bitbucket.config;

import com.orion.bitbucket.entity.ITopEntity;
import com.orion.bitbucket.entity.pull_request.PREntity;
import com.orion.bitbucket.entity.project.ProjectEntity;
import com.orion.bitbucket.entity.pull_request.asrv.McpCoreRootEntity;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EntityConfig {
    private ITopEntity entity;
    @Bean
    public ITopEntity getAllPRSEntity(){
        McpCoreRootEntity mcpCoreRootEntity = new McpCoreRootEntity();
        return mcpCoreRootEntity;
    }

    @Bean
    public PREntity getAllPrEntity(){
        McpCoreRootEntity mcpCoreRootEntity = new McpCoreRootEntity();
        return mcpCoreRootEntity;
    }

    @Bean
    public ProjectEntity getProjectsEntity(){
        ProjectEntity projectEntity = new ProjectEntity();
        return projectEntity;
    }


}
