package com.bjcareer.GPTService.out.persistence.document;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import com.bjcareer.GPTService.domain.gpt.GPTNewsDomain;

@EnableMongoRepositories
public interface GPTStockNewsRepository extends MongoRepository<GPTNewsDomain, String> {
	Optional<GPTNewsDomain> findByLink(String link);
}
