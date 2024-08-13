package com.bjcareer.search.repository.cache;

import java.util.Optional;

import org.bson.Document;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Repository;

import com.bjcareer.search.repository.noSQL.MongoTrieRepository;
import com.bjcareer.search.retrieval.cache.CacheNode;
import com.bjcareer.search.retrieval.noSQL.MongoQueryKeywords;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class CacheRepository {
	private final RedissonClient redissonClient;
	private final MongoTrieRepository repository;
	private static final String BUCKET = "TRIE:";

	public Optional<CacheNode> findByKeyword(String keyword) {
		String BUCKET_KEY = BUCKET + keyword;
		RBucket<Object> bucket = redissonClient.getBucket(BUCKET_KEY);
		if (bucket.isExists()) {
			return Optional.of((CacheNode)bucket.get());
		}
		return Optional.empty();
	}

	public void saveKeyword(CacheNode node) {
		String BUCKET_KEY = BUCKET + node.getKeyword();
		RBucket<Object> bucket = redissonClient.getBucket(BUCKET_KEY);
		bucket.set(node);
	}
}
