package com.bjcareer.search.out.repository.cache;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.bson.Document;
import org.redisson.api.RBucket;
import org.redisson.api.RScoredSortedSet;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Repository;

import com.bjcareer.search.candidate.cache.CacheNode;
import com.bjcareer.search.out.repository.noSQL.DocumentRepository;

@Repository
public class CacheRepository {
	public static final String RANK_BUCKET = "RANKING_KEYWORD";
	public static final String TRIE_BUCKET = "TRIE:";
	public static final int UPDATE_COUNT = -1;
	private final RedissonClient redissonClient;
	private final DocumentRepository repository;

	public CacheRepository(RedissonClient redissonClient, DocumentRepository repository) {
		this.redissonClient = redissonClient;
		this.repository = repository;
	}

	public Optional<CacheNode> findByKeyword(String keyword) {
		String key = TRIE_BUCKET + keyword;
		RBucket<CacheNode> bucket = redissonClient.getBucket(key);

		if (bucket.isExists()) {
			return Optional.of(bucket.get());
		}

		Document singleByKeyword = repository.findSingleByKeyword(keyword);
		CacheNode node = new CacheNode(keyword, repository.getkeyworkList(singleByKeyword));
		saveKeyword(node);

		return Optional.empty();
	}

	public void saveKeyword(CacheNode node) {
		String key = TRIE_BUCKET + node.getKeyword();
		RBucket<Object> bucket = redissonClient.getBucket(key);
		bucket.set(node);
	}

	public Double updateRanking(String keyword) {
		RScoredSortedSet<Object> scoredSortedSet = redissonClient.getScoredSortedSet(RANK_BUCKET);
		return scoredSortedSet.addScore(keyword, UPDATE_COUNT);
	}

	public List<String> getRanking(Integer rank) {
		RScoredSortedSet<Object> scoredSortedSet = redissonClient.getScoredSortedSet(RANK_BUCKET);
		return scoredSortedSet.valueRange(0, rank).stream().map(Object::toString).collect(Collectors.toList());
	}
}
