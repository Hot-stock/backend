package com.bjcareer.search.out.persistence.cache;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.redisson.api.BatchResult;
import org.redisson.api.RBatch;
import org.redisson.api.RBucket;
import org.redisson.api.RBucketAsync;
import org.redisson.api.RKeys;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Repository;

import com.bjcareer.search.application.port.out.persistence.ranking.MarketRankingPort;
import com.bjcareer.search.config.AppConfig;
import com.bjcareer.search.domain.News;
import com.bjcareer.search.domain.entity.Stock;
import com.bjcareer.search.domain.gpt.GPTStockNewsDomain;
import com.bjcareer.search.out.persistence.cache.dtos.RedisRankingStockDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Repository
@RequiredArgsConstructor
@Slf4j
public class RedisMarketRankAdapter implements MarketRankingPort {
	private final RedissonClient redissonClient;
	private final static String BUKET_KEY = "MARKET_RANKING_NEWS:";
	private final ObjectMapper mapper = AppConfig.customObjectMapper();
	public boolean isExistInCache(Stock stock) {
		String key = BUKET_KEY + stock.getName();
		return redissonClient.getBucket(key).isExists();
	}

	@Override
	public void updateRankingNews(GPTStockNewsDomain news, Stock stock) {
		String key = BUKET_KEY + stock.getName();
		RBucket<GPTStockNewsDomain> bucket = redissonClient.getBucket(key);
		bucket.set(news, Duration.ofMinutes(10));
	}

	@Override
	public List<GPTStockNewsDomain> getRankingNews() {
		List<String> keys = scanKeys(BUKET_KEY + "*");
		List<GPTStockNewsDomain> result = new ArrayList<>();

		// RBatch 생성
		RBatch batch = redissonClient.createBatch();
		List<RBucketAsync<String>> asyncBuckets = new ArrayList<>();

		// 각 키에 대한 RBucket 가져오기
		keys.forEach(key -> {
			RBucketAsync<String> bucket = batch.getBucket(key);
			bucket.getAsync();
			asyncBuckets.add(bucket);
		});

		BatchResult<?> batchResult = batch.execute();

		for (int i = 0; i < asyncBuckets.size(); i++) {
			String data = (String)batchResult.getResponses().get(i);
			Optional<RedisRankingStockDTO> redisRankingStockDTO = jsonParser(data);

			if (redisRankingStockDTO.isPresent()) {
				News news = new News(redisRankingStockDTO.get().getTitle(), redisRankingStockDTO.get().getNewsURL(),
					redisRankingStockDTO.get().getImageURL(), "", "", "");
				GPTStockNewsDomain gtpNewsDomain = new GPTStockNewsDomain(redisRankingStockDTO.get().getStockName(),
					redisRankingStockDTO.get().getSummary());
				gtpNewsDomain.addNewsDomain(news);
				result.add(gtpNewsDomain);
			}
		}
		return result;
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

	private Optional<RedisRankingStockDTO> jsonParser(String value) {
		try {

			return Optional.of(mapper.readValue(value, RedisRankingStockDTO.class));
		} catch (JsonProcessingException e) {
			log.error("Failed to convert object to json: {}", value);
			return Optional.empty();
		}
	}
}
