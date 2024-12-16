package com.bjcareer.GPTService.out.persistence.redis;

import org.redisson.api.RScoredSortedSet;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Repository
@RequiredArgsConstructor
@Slf4j
public class RedisTrendKeywordRankAdapter {
	private final RedissonClient redissonClient;
	public static final String RANK_BUCKET = "RANKING_KEYWORD";

	public Double updateRanking(String keyword, Double percentage) {
		RScoredSortedSet<Object> scoredSortedSet = redissonClient.getScoredSortedSet(RANK_BUCKET);
		scoredSortedSet.add(-percentage, keyword);
		return percentage;
	}
}
