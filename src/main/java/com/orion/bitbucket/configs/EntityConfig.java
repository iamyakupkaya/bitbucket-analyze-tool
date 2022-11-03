package com.orion.bitbucket.configs;

import com.orion.bitbucket.entities.ITopEntity;
import com.orion.bitbucket.entities.PRSEntities.AllPrsEntity;
import com.orion.bitbucket.entities.projectEntities.ProjectEntity;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EntityConfig {
    private ITopEntity entity;
    @Bean
    public ITopEntity getAllPRSEntity(){
        AllPrsEntity allPrsEntity = new AllPrsEntity();
        return allPrsEntity;
    }

    @Bean
    public ITopEntity getProjectsEntity(){
        ProjectEntity projectEntity = new ProjectEntity();
        return projectEntity;
    }


}
