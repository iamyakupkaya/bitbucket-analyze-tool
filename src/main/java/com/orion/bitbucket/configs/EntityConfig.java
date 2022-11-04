package com.orion.bitbucket.configs;

import com.orion.bitbucket.entities.ITopEntity;
import com.orion.bitbucket.entities.PRSEntities.PRSEntity;
import com.orion.bitbucket.entities.projectEntities.ProjectEntity;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EntityConfig {
    private ITopEntity entity;
    @Bean
    public ITopEntity getAllPRSEntity(){
        PRSEntity allPrsEntity = new PRSEntity();
        return allPrsEntity;
    }

    @Bean
    public ITopEntity getProjectsEntity(){
        ProjectEntity projectEntity = new ProjectEntity();
        return projectEntity;
    }


}