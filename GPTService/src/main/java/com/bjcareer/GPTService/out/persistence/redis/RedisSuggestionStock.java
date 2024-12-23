package com.bjcareer.GPTService.out.persistence.redis;

import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class RedisSuggestionStock {
	private final RedissonClient redissonClient;
	private final static String BUKET_KEY = "SUGGESTION";

	public void removeBucket() {
		redissonClient.getSet(BUKET_KEY).delete();
	}

	public void updateSuggestionStock(String stockName) {
		redissonClient.getSet(BUKET_KEY).add(stockName);
	}
}
