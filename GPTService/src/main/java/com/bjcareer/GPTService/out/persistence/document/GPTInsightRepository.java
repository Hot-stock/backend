package com.bjcareer.GPTService.out.persistence.document;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import com.bjcareer.GPTService.domain.gpt.insight.GPTInsight;

@EnableMongoRepositories
public interface GPTInsightRepository extends MongoRepository<GPTInsight, String> {
}
