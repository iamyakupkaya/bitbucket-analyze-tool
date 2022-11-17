package com.orion.bitbucket.config;
import com.orion.bitbucket.entity.pull_request.PREntity;
import com.orion.bitbucket.entity.project.ProjectEntity;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
public class EntityConfig {

    @Bean
    public PREntity getPullRequestEntity(){
        PREntity entity = new PREntity();
        return entity;
    }


    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public PREntity getPrototypePullRequestEntity(){
        PREntity entity = new PREntity();
        return entity;
    }

    @Bean
    public ProjectEntity getProjectsEntity(){
        ProjectEntity projectEntity = new ProjectEntity();
        return projectEntity;
    }

}
