package com.bjcareer.search.out.persistence.cache;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.bson.Document;
import org.redisson.api.RBucket;
import org.redisson.api.RScoredSortedSet;
import org.redisson.api.RedissonClient;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Repository;

import com.bjcareer.search.candidate.cache.CacheNode;
import com.bjcareer.search.out.persistence.noSQL.DocumentTrieRepository;

@Repository
public class CacheRepository {
	public static final String RANK_BUCKET = "RANKING_KEYWORD";
	public static final String TRIE_BUCKET = "TRIE:";
	private final RedissonClient redissonClient;
	private final DocumentTrieRepository repository;

	public CacheRepository(RedissonClient redissonClient, DocumentTrieRepository repository) {
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

	public Double updateRanking(String keyword, Double percentage) {
		RScoredSortedSet<Object> scoredSortedSet = redissonClient.getScoredSortedSet(RANK_BUCKET);
		return scoredSortedSet.addScore(keyword, -percentage);
	}

	// 반환 타입을 Pair 형태로 리팩터링
	public List<Pair<String, Double>> getRanking(Integer rank) {
		RScoredSortedSet<Object> scoredSortedSet = redissonClient.getScoredSortedSet(RANK_BUCKET);

		// entryRange로 점수와 값을 가져옴
		return scoredSortedSet.entryRange(0, rank - 1).stream()
			.map(entry -> Pair.of(entry.getValue().toString(), entry.getScore()))
			.collect(Collectors.toList());
	}

	public void deleteRanking(String keyword) {
		RScoredSortedSet<Object> scoredSortedSet = redissonClient.getScoredSortedSet(RANK_BUCKET);
		scoredSortedSet.remove(keyword);
	}

	public Double getRankingScore(String keyword) {
		RScoredSortedSet<Object> scoredSortedSet = redissonClient.getScoredSortedSet(RANK_BUCKET);
		return scoredSortedSet.getScore(keyword);
	}
}
