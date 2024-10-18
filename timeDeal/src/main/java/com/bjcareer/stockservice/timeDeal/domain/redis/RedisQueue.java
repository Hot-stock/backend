package com.bjcareer.stockservice.timeDeal.domain.redis;

import java.util.Map;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;

import org.redisson.api.BatchOptions;
import org.redisson.api.RBatch;
import org.redisson.api.RFuture;
import org.redisson.api.RScoredSortedSet;
import org.redisson.api.RScoredSortedSetAsync;
import org.redisson.api.RSetAsync;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import com.bjcareer.stockservice.timeDeal.domain.event.exception.DuplicateParticipationException;
import com.bjcareer.stockservice.timeDeal.domain.redis.VO.ParticipationVO;
import com.bjcareer.stockservice.timeDeal.application.ports.TimeDealService;
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

	public Integer addParticipation(String eventQueueKey, String participantSetKey, String clientPK) {
		RBatch batch = createBatch();

		log.debug("{} {}", eventQueueKey, participantSetKey);

		RSetAsync<String> set = batch.getSet(participantSetKey);
		RFuture<Boolean> booleanRFuture = set.addAsync(clientPK);

		RScoredSortedSetAsync<String> scoredSortedSet = batch.getScoredSortedSet(eventQueueKey);
		RFuture<Integer> turnFuture = scoredSortedSet.sizeAsync();
		CompletionStage<Boolean> booleanCompletionStage = booleanRFuture.thenCompose(isNonParticipant -> {
			if (!isNonParticipant) {
				log.debug("이미 참가 신청을 한 유저입니다{} {}", eventQueueKey, participantSetKey);
				throw new DuplicateParticipationException("이미 참가 신청이 됐습니다!");
			}
			return turnFuture.thenCompose(t -> scoredSortedSet.addAsync(t.doubleValue() + 1, clientPK));
		});

		executeBatch(batch);

		if(booleanCompletionStage.toCompletableFuture().isCompletedExceptionally()){
			throw new DuplicateParticipationException("이미 참가 신청한 이력이 있습니다!");
		}

		return getFutureResult(turnFuture, "Cant get participation");
	}

	public ParticipationVO getClientInfoUsingBatch(String key) {
		String eventId = getEventId(key);
		RBatch batch = createBatch();
		RScoredSortedSetAsync<String> scoredSortedSet = batch.getScoredSortedSet(key);

		RFuture<Double> scoreFuture = scoredSortedSet.firstScoreAsync();
		RFuture<String> nameFuture = scoredSortedSet.pollFirstAsync();
		executeBatch(batch);

		return getClientScoreVOUsingAsync(scoreFuture, nameFuture);
	}

	public void removeParticipation(String key) {
		String eventId = getEventId(key);

		RBatch batch = createBatch();
		RScoredSortedSetAsync<String> scoredSortedSet = batch.getScoredSortedSet(key);
		RFuture<Integer> pqSize = scoredSortedSet.sizeAsync();

		pqSize.thenCompose(t -> {
			if (t == null || t == 0) {
				removeSet(batch, eventId);
			}
			return null;
		});

		executeBatch(batch);
	}

	private static void removeSet(RBatch batch, String eventId) {
		RSetAsync<Object> set = batch.getSet(TimeDealService.REDIS_PARTICIPANT_SET + eventId);
		set.deleteAsync();
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

	private <T> T getFutureResult(RFuture<T> future, String errorMessage) {
		try {
			return future.get();
		} catch (InterruptedException | ExecutionException e) {
			log.error("{}: {}", errorMessage, e.getMessage(), e);
			Thread.currentThread().interrupt();
			throw new RedisCommunicationExcpetion("Can't get the result from the future", e);
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

	private String getEventId(String key) {
		String[] split = key.split(":");
		return split[2];
	}
}
