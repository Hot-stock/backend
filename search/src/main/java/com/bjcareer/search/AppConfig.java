package com.bjcareer.search;

import java.util.Map;

import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.bjcareer.search.repository.cache.CacheRepository;
import com.bjcareer.search.retrieval.Trie;
import com.bjcareer.search.retrieval.cache.CacheTrieService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class AppConfig {

	@Bean
	public Trie trie(CacheRepository trieRepository) {
		return new CacheTrieService(trieRepository);
	}

	@Bean
	public Map<String, Integer> shardingKey(RedissonClient redisson) {
		RBucket<Object> bucket = redisson.getBucket("test");

		if (bucket.isExists()) {
			log.error("sharding key exists");
			return null;
		}

		return (Map<String, Integer>)bucket.get();
	}

}
