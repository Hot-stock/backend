package com.bjcareer.GPTService.out.persistence.document;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import com.bjcareer.GPTService.domain.gpt.thema.GPTStockThema;

@EnableMongoRepositories
public interface GPTStockThemaNewsRepository extends MongoRepository<GPTStockThema, String> {
	@Query("{ 'themaInfo.name' : ?0 }")
	List<GPTStockThema> findByThemaName(String themaName);
}
