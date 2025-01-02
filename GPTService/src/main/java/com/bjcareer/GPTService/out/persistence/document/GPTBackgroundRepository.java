package com.bjcareer.GPTService.out.persistence.document;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.bjcareer.GPTService.domain.gpt.GPTTriggerBackground;

public interface GPTBackgroundRepository extends MongoRepository<GPTTriggerBackground, String> {
	Optional<GPTTriggerBackground> findByThema(String thema);
}
