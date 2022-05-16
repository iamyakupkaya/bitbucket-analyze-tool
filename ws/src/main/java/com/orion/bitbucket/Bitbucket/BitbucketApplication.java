package com.orion.bitbucket.Bitbucket;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BitbucketApplication {

    public static void main(String[] args) {
        SpringApplication.run(BitbucketApplication.class, args);

    }
}
