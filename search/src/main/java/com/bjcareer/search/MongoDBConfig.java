package com.bjcareer.search;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

import jakarta.annotation.PreDestroy;

@Configuration
@ConfigurationProperties(prefix = "mongodb")
public class MongoDBConfig {

	@Value("${mongodb.uri}")
	private String uri;

	@Value("${mongodb.database}")
	private String database;

	private MongoClient mongoClient;

	@Bean
	public MongoClient mongoClient() {
		this.mongoClient = MongoClients.create(uri);
		return this.mongoClient;
	}

	@Bean
	public MongoDatabase mongoDatabase(MongoClient mongoClient) {
		return mongoClient.getDatabase(database);
	}

	@PreDestroy
	public void closeMongoClient() {
		mongoClient.close();
	}
}
