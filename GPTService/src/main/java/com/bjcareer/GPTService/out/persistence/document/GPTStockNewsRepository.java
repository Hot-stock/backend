package com.bjcareer.GPTService.out.persistence.document;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import com.bjcareer.GPTService.domain.gpt.GPTNewsDomain;

@EnableMongoRepositories
public interface GPTStockNewsRepository extends MongoRepository<GPTNewsDomain, String> {
	Optional<GPTNewsDomain> findByLink(String link);

	@Query("{ 'stockName' : ?0, 'news.pubDate' : ?1, 'isRelated' : true }")
	List<GPTNewsDomain> findByStockNameAndPubDateWithRelatedIsTrue(String stockName, LocalDate pubDate);

	List<GPTNewsDomain> findByStockName(String stockName);
}
