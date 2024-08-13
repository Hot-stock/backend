package com.bjcareer.search;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.redisson.config.SingleServerConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.bjcareer.search.repository.cache.CacheRepository;
import com.bjcareer.search.retrieval.cache.CacheTrieService;
import com.bjcareer.search.retrieval.Trie;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class AppConfig {

	@Bean
	public Trie trie(CacheRepository trieRepository) {
		return new CacheTrieService(trieRepository);
	}


	@Bean
	public RedissonClient redissonClient(){
		Config config = new Config();

		SingleServerConfig singleServerConfig = config.useSingleServer();
		singleServerConfig.setAddress("redis://localhost:6379");
		singleServerConfig.setPassword("changeme");
		singleServerConfig.setDatabase(0);

		return Redisson.create(config);
	}
}
