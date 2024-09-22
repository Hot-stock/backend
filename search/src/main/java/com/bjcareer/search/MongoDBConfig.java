package com.bjcareer.search;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;

import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

@ConfigurationProperties(
	prefix = "mongodb"
)
public class MongoDBConfig {
	@Value("${mongodb.uri}")
	private String uri;

	@Value("${mongodb.database}")
	private String database;


	@Bean
	public MongoDatabase mongoClient() {
		return MongoClients.create(uri).getDatabase(database);
	}
}
