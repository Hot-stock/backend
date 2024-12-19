package com.bjcareer.GPTService.out.persistence.redis;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import org.redisson.api.RBucket;
import org.redisson.api.RKeys;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Repository;

import com.bjcareer.GPTService.config.AppConfig;
import com.bjcareer.GPTService.domain.gpt.GPTNewsDomain;
import com.bjcareer.GPTService.out.persistence.redis.dto.RedisRankingStockDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Repository
@RequiredArgsConstructor
@Slf4j
public class RedisMarketRankAdapter {
	private final RedissonClient redissonClient;
	private final static String BUKET_KEY = "MARKET_RANKING_NEWS:";

	public boolean isExistInCache(String stocName) {
		String key = BUKET_KEY + stocName;
		return redissonClient.getBucket(key).isExists();
	}

	public void updateRankingNews(GPTNewsDomain news) {
		ObjectMapper mapper = AppConfig.customObjectMapper();

		RedisRankingStockDTO redisRankingStockDTO = new RedisRankingStockDTO(news.getStockName(),
			news.getNews().getTitle(), news.getReason(),
			news.getNews().getNewsLink(), news.getNews().getImgLink());

		try {
			String key = BUKET_KEY + news.getStockName();
			RBucket<String> bucket = redissonClient.getBucket(key);
			bucket.set(mapper.writeValueAsString(redisRankingStockDTO), Duration.ofMinutes(10));

		} catch (JsonProcessingException e) {
			log.error("Failed to convert object to json: {}", redisRankingStockDTO);
		}
	}
	
	public void updateRankingNewsByStockName(String stockName) {
		String key = BUKET_KEY + stockName;
		RBucket<String> bucket = redissonClient.getBucket(key);

		// 키가 존재하지 않을 경우 null 반환 가능
		if (bucket.isExists()) {
			bucket.expire(Duration.ofMinutes(10)); // 만료 시간만 갱신
		} else {
			// 키가 없는 경우 처리 (필요 시 초기화하거나 로그 출력)
			log.warn("Key does not exist for: " + key);
		}
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
