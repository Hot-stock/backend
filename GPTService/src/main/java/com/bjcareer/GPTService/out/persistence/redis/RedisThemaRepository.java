package com.bjcareer.GPTService.out.persistence.redis;

import java.util.List;
import java.util.stream.Collectors;

import org.redisson.api.RSet;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Repository;

import com.bjcareer.GPTService.domain.gpt.GPTNewsDomain;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class RedisThemaRepository {
	private final RedissonClient redissonClient;
	private final static String BUKET_KEY = "THEMAS";

	public void updateThema(String thema) {
		RSet<String> set = redissonClient.getSet(BUKET_KEY);
		set.add(thema);
	}

	public String loadThema() {
		RSet<String> set = redissonClient.getSet(BUKET_KEY);
		return set.stream().collect(Collectors.joining(","));
	}

	public void uploadToFailSet(GPTNewsDomain news) {
		String themaName = BUKET_KEY + ":FAIL";
		RSet<GPTNewsDomain> set = redissonClient.getSet(themaName);
		set.add(news);
	}

	public List<GPTNewsDomain> getNewsInFailSet() {
		String themaName = BUKET_KEY + ":FAIL";
		RSet<GPTNewsDomain> set = redissonClient.getSet(themaName);
		List<GPTNewsDomain> newses = set.stream().toList();
		set.delete();
		return newses;
	}
}
