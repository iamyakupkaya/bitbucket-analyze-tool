package com.orion.bitbucket.Bitbucket;

import com.orion.bitbucket.Bitbucket.service.PullRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BitbucketApplication {

	public static void main(String[] args) {
		SpringApplication.run(BitbucketApplication.class, args);
	}

}
