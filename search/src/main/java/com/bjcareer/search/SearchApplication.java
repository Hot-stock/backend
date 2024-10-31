package com.bjcareer.search;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.bjcareer.search.config.infra.MongoDBConfig;

@SpringBootApplication
@EnableScheduling
@EnableAsync
@EnableConfigurationProperties({MongoDBConfig.class})
public class SearchApplication {

	public static void main(String[] args) {
		SpringApplication.run(SearchApplication.class, args);
	}
}
