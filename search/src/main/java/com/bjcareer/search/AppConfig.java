package com.bjcareer.search;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.bjcareer.search.candidate.Trie;
import com.bjcareer.search.candidate.cache.CacheTrieService;
import com.bjcareer.search.repository.cache.CacheRepository;
import com.bjcareer.search.repository.noSQL.DocumentRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class AppConfig {

	@Bean
	public Trie trie(CacheRepository trieRepository, DocumentRepository documentRepository) {
		return new CacheTrieService(trieRepository, documentRepository);
	}
}
