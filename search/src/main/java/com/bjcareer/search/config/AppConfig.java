package com.bjcareer.search.config;

import java.time.ZoneId;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import com.bjcareer.search.candidate.Trie;
import com.bjcareer.search.candidate.cache.CacheTrieService;
import com.bjcareer.search.out.persistence.cache.CacheRepository;
import com.bjcareer.search.out.persistence.noSQL.DocumentTrieRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class AppConfig {
	public static final ZoneId ZONE_ID = ZoneId.of("Asia/Seoul");

	@Bean
	public Trie trie(CacheRepository trieRepository, DocumentTrieRepository documentRepository) {
		return new CacheTrieService(trieRepository, documentRepository);
	}

	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}

	public static ObjectMapper customObjectMapper() {
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.registerModule(new JavaTimeModule());
		objectMapper.registerModule(new Jdk8Module());
		return objectMapper;
	}
}
