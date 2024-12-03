package com.bjcareer.search.config.infra;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

import jakarta.annotation.PreDestroy;

@Configuration
public class MongoDBConfig {

	@Value("${mongodb.uri}")
	private String uri;

	private MongoClient mongoClient;

	@Bean
	public MongoClient mongoClient() {
		this.mongoClient = MongoClients.create(uri);
		return this.mongoClient;
	}
	@PreDestroy
	public void closeMongoClient() {
		mongoClient.close();
	}
}
