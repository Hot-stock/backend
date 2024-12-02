package com.bjcareer.search.out.persistence.cache;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.redisson.api.RBucket;
import org.redisson.api.RKeys;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Repository;

import com.bjcareer.search.application.port.out.persistence.ranking.MarketRankingPort;
import com.bjcareer.search.config.AppConfig;
import com.bjcareer.search.domain.News;
import com.bjcareer.search.domain.entity.Stock;
import com.bjcareer.search.domain.gpt.GTPNewsDomain;
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

	public boolean isExistInCache(Stock stock) {
		String key = BUKET_KEY + stock.getName();
		return redissonClient.getBucket(key).isExists();
	}

	@Override
	public void updateRankingNews(GTPNewsDomain news, Stock stock) {
		String key = BUKET_KEY + stock.getName();
		RBucket<GTPNewsDomain> bucket = redissonClient.getBucket(key);
		bucket.set(news, Duration.ofMinutes(10));
	}

	@Override
	public List<GTPNewsDomain> getRankingNews() {
		ObjectMapper mapper = AppConfig.customObjectMapper();
		List<String> keys = scanKeys(BUKET_KEY + "*");

		List<GTPNewsDomain> result = new ArrayList<>();

		List<RedisRankingStockDTO> list = keys.stream()
			.filter(key -> redissonClient.getBucket(key).isExists())
			.map(key -> jsonParser(mapper, (String)redissonClient.getBucket(key).get()))
			.filter(Optional::isPresent)
			.flatMap(Optional::stream).toList();

		for (RedisRankingStockDTO dto : list) {
			News news = new News(dto.getTitle(), dto.getNewsURL(), dto.getImageURL(), "", "", "");
			GTPNewsDomain gtpNewsDomain = new GTPNewsDomain(dto.getStockName(), dto.getSummary(), null, null, null);

			gtpNewsDomain.addNewsDomain(news);
			result.add(gtpNewsDomain);
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

	private Optional<RedisRankingStockDTO> jsonParser(ObjectMapper mapper, String value) {
		try {
			return Optional.of(mapper.readValue(value, RedisRankingStockDTO.class));
		} catch (JsonProcessingException e) {
			log.error("Failed to convert object to json: {}", value);
			return Optional.empty();
		}
	}
}
