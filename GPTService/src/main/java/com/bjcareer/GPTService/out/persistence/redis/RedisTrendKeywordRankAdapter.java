package com.bjcareer.GPTService.out.persistence.redis;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.redisson.api.RScoredSortedSet;
import org.redisson.api.RedissonClient;
import org.redisson.client.protocol.ScoredEntry;
import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Repository
@RequiredArgsConstructor
@Slf4j
public class RedisTrendKeywordRankAdapter {
	private final RedissonClient redissonClient;
	public static final String STOCK_RANK_BUCKET = "RANKING_KEYWORD";
	public static final String THEMA_RANK_BUCKET = "RANKING_KEYWORD:THEMA";

	public Double updateRanking(String keyword, Double percentage, String bucket) {
		RScoredSortedSet<Object> scoredSortedSet = redissonClient.getScoredSortedSet(bucket);
		scoredSortedSet.add(-percentage, keyword);
		return percentage;
	}

	public List<String> getRanking(int threshold, String bucket) {
		List<String> rankingStock = new ArrayList<>();

		RScoredSortedSet<Object> scoredSortedSet = redissonClient.getScoredSortedSet(bucket);
		Collection<ScoredEntry<Object>> scoredEntries = scoredSortedSet.entryRange(0, threshold);

		scoredEntries.forEach(t -> rankingStock.add(t.getValue().toString()));

		return rankingStock;
	}
}
