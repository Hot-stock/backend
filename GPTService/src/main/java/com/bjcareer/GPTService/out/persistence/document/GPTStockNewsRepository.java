package com.bjcareer.GPTService.out.persistence.document;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.bjcareer.GPTService.domain.gpt.GPTNewsDomain;

public interface GPTStockNewsRepository extends MongoRepository<GPTNewsDomain, String> {
	GPTNewsDomain findByLink(String stockCode);
}
