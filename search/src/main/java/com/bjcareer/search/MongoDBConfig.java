package com.bjcareer.search;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

@ConfigurationProperties(
	prefix = "mongodb"
)
public class MongoDBConfig {
	@Value("#{'${mongodb.uri}'.split(',')}")
	private List<String> uris;

	@Value("${mongodb.database}")
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