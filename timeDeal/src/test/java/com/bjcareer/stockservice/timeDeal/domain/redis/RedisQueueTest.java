package com.bjcareer.stockservice.timeDeal.domain.redis;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.redisson.api.RScoredSortedSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.bjcareer.stockservice.timeDeal.domain.redis.VO.ParticipationVO;

@SpringBootTest
class RedisQueueTest {
	public static final String TEST_KEY = "TEST_KEY";
	@Autowired RedisQueue redisQueue;
	private RScoredSortedSet<String> scoredSortedSet;

	@BeforeEach
	void setUp() {
		scoredSortedSet = redisQueue.getScoredSortedSet(TEST_KEY);
	}

	@Test
	void testRetrieveInTimeOrder() {
		for (int i = 0; i < 10; i++) {
			scoredSortedSet.add(i, "USER" + i);
		}

		for (int i = 0; i < 10; i++) {
			Double v = scoredSortedSet.firstScore();
			String s = scoredSortedSet.pollFirst();
			ParticipationVO participationVO = new ParticipationVO(s, v);

			assertEquals("USER" + i, participationVO.getClientId());
			assertEquals(i, participationVO.getScore());
		}
	}

	@Test
	void testBatchOperation() {
		int i = 0;
		scoredSortedSet.add(i, "USER" + i);
		ParticipationVO clientInfoUsingBatch = redisQueue.getClientInfoUsingBatch(TEST_KEY);
		assertEquals(i, clientInfoUsingBatch.getScore());
	}

	@Test
	void testForConcurrencyIssues() {
		List<ParticipationVO> list = new ArrayList<>();
		for (int i = 0; i < 10; i++) {
			scoredSortedSet.add(i, "USER" + i);
		}

		ExecutorService executorService = Executors.newFixedThreadPool(10);

		for (int i = 0; i < 10; i++) {
			executorService.submit(() -> {
				ParticipationVO clientInfoUsingBatch = redisQueue.getClientInfoUsingBatch(TEST_KEY);
				list.add(clientInfoUsingBatch);
			});
		}

		executorService.shutdown();
		try {
			executorService.awaitTermination(10, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}

		for (ParticipationVO participationVO : list) {
			String answer = "USER" + participationVO.getScore().intValue();
			assertTrue(answer.equals(participationVO.getClientId()));
			System.out.println(answer.equals(participationVO.getClientId()));
		}
	}
}
