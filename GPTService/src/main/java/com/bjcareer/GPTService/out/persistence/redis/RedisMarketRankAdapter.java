package com.bjcareer.GPTService.out.persistence.redis;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import org.redisson.api.RBucket;
import org.redisson.api.RKeys;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Repository;

import com.bjcareer.GPTService.domain.gpt.GPTNewsDomain;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class RedisMarketRankAdapter {
	private final RedissonClient redissonClient;
	private final static String BUKET_KEY = "KAKFA:MARKET_RANKING_NEWS:";

	public boolean isExistInCache(String stocName) {
		String key = BUKET_KEY + stocName;
		return redissonClient.getBucket(key).isExists();
	}

	public void updateRankingNews(GPTNewsDomain news) {
		String key = BUKET_KEY + news.getStockName();
		RBucket<GPTNewsDomain> bucket = redissonClient.getBucket(key);
		bucket.set(news, Duration.ofMinutes(10));
	}

	public List<GPTNewsDomain> getRankingNews() {
		List<String> keys = scanKeys(BUKET_KEY + "*");

		return keys.stream()
			.filter(key -> redissonClient.getBucket(key).isExists())
			.map(key -> (GPTNewsDomain)redissonClient.getBucket(key).get())
			.toList();

	}

	public void removeRankingNews(GPTNewsDomain news) {
		String key = BUKET_KEY + news.getStockName();
		redissonClient.getBucket(key).delete();
	}

	private List<String> scanKeys(String pattern) {
		RKeys keys = redissonClient.getKeys();
		Iterable<String> foundKeys = keys.getKeysByPattern(pattern);
		List<String> keyList = new ArrayList<>();

		foundKeys.forEach(keyList::add);

		return keyList;
	}
}
