package com.bjcareer.stockservice.timeDeal.domain.redis;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;

import org.redisson.api.BatchOptions;
import org.redisson.api.RBatch;
import org.redisson.api.RFuture;
import org.redisson.api.RScoredSortedSet;
import org.redisson.api.RScoredSortedSetAsync;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import com.bjcareer.stockservice.timeDeal.domain.redis.VO.ParticipationVO;
import com.bjcareer.stockservice.timeDeal.service.exception.RedisCommunicationExcpetion;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class RedisQueue {

	private final RedissonClient client;
	private final Map<String, RScoredSortedSet<?>> cachePQ = new ConcurrentHashMap<>();

	@SuppressWarnings("unchecked")
	public <T> RScoredSortedSet<T> getScoredSortedSet(String name) {
		return (RScoredSortedSet<T>) cachePQ.computeIfAbsent(name, client::getScoredSortedSet);
	}

	public Integer addParticipation(String key, String clientPK) {
		RBatch batch = createBatch();
		RScoredSortedSetAsync<String> scoredSortedSet = batch.getScoredSortedSet(key);

		RFuture<Integer> turnFuture = scoredSortedSet.sizeAsync();
		turnFuture.thenCompose(t -> scoredSortedSet.addAsync(t.doubleValue() + 1, clientPK));
		executeBatch(batch);

		return getFutureResult(turnFuture, "Error adding participation");
	}

	public ParticipationVO getClientInfoUsingBatch(String key) {
		RBatch batch = createBatch();
		RScoredSortedSetAsync<String> scoredSortedSet = batch.getScoredSortedSet(key);

		RFuture<Double> scoreFuture = scoredSortedSet.firstScoreAsync();
		RFuture<String> nameFuture = scoredSortedSet.pollFirstAsync();

		executeBatch(batch);

		return getClientScoreVOUsingAsync(scoreFuture, nameFuture);
	}

	private RBatch createBatch() {
		return client.createBatch(BatchOptions.defaults().executionMode(BatchOptions.ExecutionMode.REDIS_WRITE_ATOMIC));
	}

	private void executeBatch(RBatch batch) {
		try {
			batch.execute();
		} catch (Exception e) {
			log.error("Batch execution failed: {}", e.getMessage(), e);
			throw new RedisCommunicationExcpetion("Server is unstable. Please try again.");
		}
	}

	private Integer getFutureResult(RFuture<Integer> future, String errorMessage) {
		try {
			return future.get();
		} catch (InterruptedException | ExecutionException e) {
			log.error("{}: {}", errorMessage, e.getMessage(), e);
			Thread.currentThread().interrupt();
			throw new RedisCommunicationExcpetion("Can't get of the scored size");
		}
	}

	private ParticipationVO getClientScoreVOUsingAsync(RFuture<Double> scoreFuture, RFuture<String> nameFuture) {
		try {
			Double score = scoreFuture.get();
			String name = nameFuture.get();
			return new ParticipationVO(name, score);
		} catch (InterruptedException | ExecutionException e) {
			log.error("Error retrieving client score: {}", e.getMessage(), e);
			Thread.currentThread().interrupt();
			return null;
		}
	}
}
