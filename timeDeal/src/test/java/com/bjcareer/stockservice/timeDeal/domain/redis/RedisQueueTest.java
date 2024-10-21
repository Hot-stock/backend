package com.bjcareer.stockservice.timeDeal.domain.redis;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.redisson.api.RScoredSortedSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.bjcareer.stockservice.timeDeal.domain.ParticipationDomain;

@SpringBootTest
class RedisQueueTest {
	public static final String TEST_KEY = "TEST_KEY";
	@Autowired RedisQueue redisQueue;
	private RScoredSortedSet<ParticipationDomain> scoredSortedSet;

	@BeforeEach
	void setUp() {
		scoredSortedSet = redisQueue.getScoredSortedSet(TEST_KEY);
	}

	@Test
	void testRetrieveInTimeOrder() {
		for (int i = 0; i < 10; i++) {
			scoredSortedSet.add(i, new ParticipationDomain("USER" + i, UUID.randomUUID().toString()));
		}

		for (int i = 0; i < 10; i++) {
			Double v = scoredSortedSet.firstScore();
			ParticipationDomain participationDomain = scoredSortedSet.pollFirst();

			assertEquals("USER" + i, participationDomain.getClientId());
			assertEquals(i, participationDomain.getParticipationIndex());
		}
	}

	@Test
	void testBatchOperation() {
		int i = 0;

		scoredSortedSet.add(i, new ParticipationDomain("USER" + i, UUID.randomUUID().toString()));
		ParticipationDomain clientInfoUsingBatch = redisQueue.getClientInfoUsingBatch(TEST_KEY);
		assertEquals(i, clientInfoUsingBatch.getParticipationIndex());
	}

	@Test
	void testForConcurrencyIssues() {
		List<ParticipationDomain> list = new ArrayList<>();
		for (int i = 0; i < 10; i++) {
			scoredSortedSet.add(i, new ParticipationDomain("USER" + i, UUID.randomUUID().toString()));
		}

		ExecutorService executorService = Executors.newFixedThreadPool(10);

		for (int i = 0; i < 10; i++) {
			executorService.submit(() -> {
				ParticipationDomain clientInfoUsingBatch = redisQueue.getClientInfoUsingBatch(TEST_KEY);
				list.add(clientInfoUsingBatch);
			});
		}

		executorService.shutdown();
		try {
			executorService.awaitTermination(10, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}

		for (ParticipationDomain participationDomain : list) {
			String answer = "USER" + participationDomain.getParticipationIndex().intValue();
			assertTrue(answer.equals(participationDomain.getClientId()));
			System.out.println(answer.equals(participationDomain.getClientId()));
		}
	}
}
