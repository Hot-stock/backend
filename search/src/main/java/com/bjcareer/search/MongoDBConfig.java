package com.bjcareer.search;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

@Configuration
public class MongoDBConfig {

	@Value("${spring.data.mongodb.uris}")
	private List<String> uris;

	@Value("${spring.data.mongodb.database}")
	private String database;

	@Bean
	public List<MongoDatabase> mongoClient() {
		List<MongoDatabase> databases = new ArrayList<>();
		for (String uri : uris) {
			databases.add(MongoClients.create(uri).getDatabase(database));
		}
		return databases;
	}
}