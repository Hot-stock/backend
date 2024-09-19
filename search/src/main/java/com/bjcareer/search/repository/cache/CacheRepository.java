package com.bjcareer.search.repository.cache;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.bson.Document;
import org.redisson.api.RBucket;
import org.redisson.api.RScoredSortedSet;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Repository;

import com.bjcareer.search.repository.noSQL.DocumentRepository;
import com.bjcareer.search.retrieval.cache.CacheNode;
import com.bjcareer.search.retrieval.noSQL.DocumentQueryKeywords;

@Repository
public class CacheRepository {
	public static final String RANK_BUCKET = "RANKING_KEYWORD";
	public static final String TRIE_BUCKET = "TRIE:";

	private final RedissonClient redissonClient;
	private final DocumentRepository repository;
	private final Map<String, Integer> shardingKey;

	public CacheRepository(RedissonClient redissonClient, DocumentRepository repository,
		Map<String, Integer> shardingKey) {
		this.redissonClient = redissonClient;
		this.repository = repository;
		this.shardingKey = shardingKey;
	}

	public Optional<CacheNode> findByKeyword(String keyword) {
		String key = TRIE_BUCKET + keyword;
		RBucket<Object> bucket = redissonClient.getBucket(key);

		if (bucket.isExists()) {
			return Optional.of((CacheNode)bucket.get());
		}

		Document singleByKeyword = repository.findSingleByKeyword(DocumentQueryKeywords.KEYWORD, keyword);
		CacheNode node = new CacheNode(keyword, repository.getkeyworkList(keyword, singleByKeyword));
		saveKeyword(node);

		return Optional.empty();
	}

	public void saveKeyword(CacheNode node) {
		String BUCKET_KEY = TRIE_BUCKET + node.getKeyword();
		RBucket<Object> bucket = redissonClient.getBucket(BUCKET_KEY);
		bucket.set(node);
	}

	public Double updateRanking(String keyword) {
		RScoredSortedSet<Object> scoredSortedSet = redissonClient.getScoredSortedSet(RANK_BUCKET);
		return scoredSortedSet.addScore(keyword, -1);
	}

	public List<String> getRanking(Integer rank) {
		RScoredSortedSet<Object> scoredSortedSet = redissonClient.getScoredSortedSet(RANK_BUCKET);
		return scoredSortedSet.valueRange(0, rank).stream().map(Object::toString).collect(Collectors.toList());
	}
}
