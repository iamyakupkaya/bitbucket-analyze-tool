package com.orion.bitbucket.Bitbucket;

import com.orion.bitbucket.Bitbucket.dbc.DatabaseManager;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BitbucketApplication {

    public static void main(String[] args) {
        DatabaseManager.run();
        SpringApplication.run(BitbucketApplication.class, args);
    }
}
