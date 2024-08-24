package com.bjcareer.stockservice.timeDeal.domain.redis;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.redisson.api.BatchOptions;
import org.redisson.api.BatchResult;
import org.redisson.api.RBatch;
import org.redisson.api.RFuture;
import org.redisson.api.RScoredSortedSet;
import org.redisson.api.RScoredSortedSetAsync;
import org.redisson.api.RTransaction;
import org.redisson.api.RedissonClient;
import org.redisson.api.TransactionOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.bjcareer.stockservice.timeDeal.domain.redis.VO.ParticipationVO;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class RedisQueue {
	private final RedissonClient client;
	Map<String, RScoredSortedSet<?>> cachePQ = new ConcurrentHashMap<>();

	public <T> RScoredSortedSet<T> getScoredSortedSet(String name) {
		RScoredSortedSet<T> pq = (RScoredSortedSet<T>) cachePQ.get(name);

		if (pq == null) {
			cachePQ.put(name, client.getScoredSortedSet(name));
		}

		return (RScoredSortedSet<T>)cachePQ.get(name);
	}

	public void removeCacheScoredSortedSetIfInNodata(String key) {
		RScoredSortedSet<?> objects = cachePQ.get(key);
		if (objects != null) {
			if (objects.isEmpty())
				cachePQ.remove(key);
		}
	}

	public ParticipationVO getClientInfoUsingBatch(String key){
		RBatch batch = client.createBatch(BatchOptions.defaults().executionMode(BatchOptions.ExecutionMode.REDIS_WRITE_ATOMIC));
		RScoredSortedSetAsync<String> scoredSortedSet = batch.getScoredSortedSet(key);

		RFuture<Double> scoreFuture = scoredSortedSet.firstScoreAsync();
		RFuture<String> nameFuture = scoredSortedSet.pollFirstAsync();

		batch.execute();

		return getClientScoreVOUsingAsync(scoreFuture, nameFuture);
	}

	private ParticipationVO getClientScoreVOUsingAsync(RFuture<Double> scoreFuture, RFuture<String> nameFuture) {
		try {
			Double score = scoreFuture.get();
			String name = nameFuture.get();
			return new ParticipationVO(name, score);
		} catch (Exception e) {
			System.out.println(e.getMessage());
			log.error(e.getMessage());
			return null;
		}
	}
}
