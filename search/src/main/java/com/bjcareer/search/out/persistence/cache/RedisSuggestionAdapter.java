package com.bjcareer.search.out.persistence.cache;

import java.util.List;

import org.redisson.api.RSet;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class RedisSuggestionAdapter {
	private final RedissonClient redissonClient;
	private final static String BUKET_KEY = "SUGGESTION";

	public List<String> getSuggestionStock() {
		RSet<String> set = redissonClient.getSet(BUKET_KEY);
		return set.readAll().stream().toList();
	}
}
