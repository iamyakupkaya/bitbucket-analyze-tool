package com.orion.bitbucket.config;

import com.google.gson.Gson;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UtilConfig {
    @Bean
    public Gson getGson(){
        return new Gson();
    }
}
