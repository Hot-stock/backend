package com.bjcareer.GPTService.out.persistence.document;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import com.bjcareer.GPTService.domain.gpt.GPTNewsDomain;
import com.bjcareer.GPTService.domain.gpt.thema.GPTThema;

@EnableMongoRepositories
public interface GPTThemaNewsRepository extends MongoRepository<GPTThema, String> {
	Optional<GPTThema> findByLink(String link);
}
