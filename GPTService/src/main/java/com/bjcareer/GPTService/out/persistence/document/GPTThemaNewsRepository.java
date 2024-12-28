package com.bjcareer.GPTService.out.persistence.document;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import com.bjcareer.GPTService.domain.gpt.thema.GPTThema;

@EnableMongoRepositories
public interface GPTThemaNewsRepository extends MongoRepository<GPTThema, String> {
	List<GPTThema> findByLinkIn(List<String> links);

	Optional<GPTThema> findByLink(String link);

	@Query("{ 'themaInfo.name' : ?0, 'isRelatedThema' : true }")
	List<GPTThema> findThemaNews(String themaName);
}
