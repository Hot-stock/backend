package com.bjcareer.GPTService.out.persistence.document;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import com.bjcareer.GPTService.domain.gpt.thema.GPTStockThema;

@EnableMongoRepositories
public interface GPTThemaNewsRepository extends MongoRepository<GPTStockThema, String> {
	Optional<GPTStockThema> findByLink(String link);
	@Query("{ 'themaInfo.name' : ?0 }")
	List<GPTStockThema> findByThemaName(String themaName); // 'name' 필드 기반 쿼리

	@Query("{ 'themaInfo.stockName' : ?0 }")
	List<GPTStockThema> findByStockName(String stockName); // 'name' 필드 기반 쿼리
}
