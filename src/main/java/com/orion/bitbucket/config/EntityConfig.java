package com.orion.bitbucket.config;

import com.orion.bitbucket.entity.ITopEntity;
import com.orion.bitbucket.entity.pull_request.PREntity;
import com.orion.bitbucket.entity.project.ProjectEntity;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EntityConfig {
    private ITopEntity entity;
    @Bean
    public ITopEntity getAllPRSEntity(){
        PREntity allPrsEntity = new PREntity();
        return allPrsEntity;
    }

    @Bean
    public ITopEntity getProjectsEntity(){
        ProjectEntity projectEntity = new ProjectEntity();
        return projectEntity;
    }


}
