package com.orion.bitbucket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.data.mongodb.core.MongoTemplate;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class })
public class BitbucketApplication implements CommandLineRunner {
	@Autowired
	private MongoTemplate mongoTemplate;

	public static void main(String[] args) {
		SpringApplication.run(BitbucketApplication.class, args);

	}

	@Override
	public void run(String... args) throws Exception {

	}
}
