package com.bjcareer.search.repository.cache;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.bson.Document;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.bjcareer.search.repository.noSQL.DocumentRepository;
import com.bjcareer.search.retrieval.cache.CacheNode;
import com.bjcareer.search.retrieval.noSQL.DocumentQueryKeywords;

import lombok.RequiredArgsConstructor;

@Repository
public class CacheRepository {
	private final List<RedissonClient> redissonClients;
	private final DocumentRepository repository;
	private static final String BUCKET = "TRIE:";
	private final Map<String, Integer> shardingKey;

	public CacheRepository(
		@Qualifier("primaryRedissonClient")List<RedissonClient> redissonClients, DocumentRepository repository,
		Map<String, Integer> shardingKey) {
		this.redissonClients = redissonClients;
		this.repository = repository;
		this.shardingKey = shardingKey;
	}

	public Optional<CacheNode> findByKeyword(String keyword) {
		String BUCKET_KEY = BUCKET + keyword;

		RedissonClient redissonClient = getRedissonClient(keyword);
		RBucket<Object> bucket = redissonClient.getBucket(BUCKET_KEY);

		if (bucket.isExists()) {
			return Optional.of((CacheNode)bucket.get());
		}

		Document singleByKeyword = repository.findSingleByKeyword(DocumentQueryKeywords.KEYWORD, keyword);
		CacheNode node = new CacheNode(keyword, repository.getkeyworkList(keyword, singleByKeyword));
		saveKeyword(node);

		return Optional.empty();
	}

	public void saveKeyword(CacheNode node) {
		String BUCKET_KEY = BUCKET + node.getKeyword();
		RedissonClient redissonClient = getRedissonClient(node.getKeyword());
		RBucket<Object> bucket = redissonClient.getBucket(BUCKET_KEY);
		bucket.set(node);
	}

	private RedissonClient getRedissonClient(String keyword) {
		Integer i = shardingKey.get(keyword);
		System.out.println("i = " + i);
		System.out.println("redissonClients = " + redissonClients);
		RedissonClient redissonClient = redissonClients.get(i);
		return redissonClient;
	}
}
