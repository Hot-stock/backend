package com.bjcareer.search;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.redisson.Redisson;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.redisson.config.SingleServerConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.bjcareer.search.repository.cache.CacheRepository;
import com.bjcareer.search.repository.noSQL.DocumentRepository;
import com.bjcareer.search.retrieval.Trie;
import com.bjcareer.search.retrieval.cache.CacheNode;
import com.bjcareer.search.retrieval.cache.CacheTrieService;
import com.bjcareer.search.retrieval.noSQL.DocumentTrieService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class AppConfig {

	@Value("${database.number}")
	private int databaseNumber;

	@Value("${database.shardingKey}")
	private String shardingKey;

	@Bean
	public Trie trie(CacheRepository trieRepository) {
		return new CacheTrieService(trieRepository);
	}

	@Bean
	public Map<String, Integer> shardingKey(RedissonClient redisson){
		RBucket<Object> bucket = redisson.getBucket(shardingKey);

		if (bucket.isExists()) {
			log.error("sharding key exists");
			return null;
		}

		return (Map<String, Integer>)bucket.get();
	}
}
