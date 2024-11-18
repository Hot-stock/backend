package com.bjcareer.search.out.persistence.repository.cache;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import org.redisson.api.RBucket;
import org.redisson.api.RKeys;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Repository;

import com.bjcareer.search.application.port.out.persistence.ranking.MarketRankingPort;
import com.bjcareer.search.domain.entity.Stock;
import com.bjcareer.search.domain.gpt.GTPNewsDomain;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class RedisMarketRankAdapter implements MarketRankingPort {
	private final RedissonClient redissonClient;
	private final static String BUKET_KEY = "MARKET_RANKING_NEWS:";

	public boolean isExistInCache(Stock stock) {
		String key = BUKET_KEY + stock.getName();
		return redissonClient.getBucket(key).isExists();
	}

	@Override
	public void updateRankingNews(GTPNewsDomain news, Stock stock) {
		String key = BUKET_KEY + stock.getName();
		RBucket<GTPNewsDomain> bucket = redissonClient.getBucket(key);
		bucket.set(news, Duration.ofMinutes(2));
	}

	@Override
	public List<GTPNewsDomain> getRankingNews() {
		List<String> keys = scanKeys(BUKET_KEY + "*");

		return keys.stream()
			.filter(key -> redissonClient.getBucket(key).isExists())
			.map(key -> (GTPNewsDomain)redissonClient.getBucket(key).get())
			.toList();

	}

	@Override
	public void removeRankingNews(Stock stock) {
		String key = BUKET_KEY + stock.getName();
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
