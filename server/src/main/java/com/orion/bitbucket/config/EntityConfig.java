package com.orion.bitbucket.config;
import com.orion.bitbucket.entity.pull_request.PREntity;
import com.orion.bitbucket.entity.project.ProjectEntity;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EntityConfig {

    @Bean
    public PREntity getPullRequestEntity(){
        PREntity entity = new PREntity();
        return entity;
    }


    @Bean
    public ProjectEntity getProjectsEntity(){
        ProjectEntity projectEntity = new ProjectEntity();
        return projectEntity;
    }


}
