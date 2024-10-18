package com.bjcareer.stockservice.timeDeal.service;


import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.redisson.api.RScoredSortedSet;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.bjcareer.stockservice.timeDeal.application.ports.TimeDealService;
import com.bjcareer.stockservice.timeDeal.domain.redis.Redis;
import com.bjcareer.stockservice.timeDeal.domain.redis.RedisQueue;

@SpringBootTest
class CronQueueServiceTest {
	public static final String TEST_KEY = TimeDealService.REDIS_QUEUE_NAME + "987654321";
	public static final String TEST_PARTICIPANT = TimeDealService.REDIS_PARTICIPANT_SET + "987654321";

	@Autowired
	private TimeDealService timeDealService;

	@Autowired
	private RedisQueue redisQueue;

	@Autowired
	private Redis redis;

	@Autowired
	private CronQueueService cronQueueService;

	@Autowired
	private RedissonClient client;


	@BeforeEach
	void setUp() {
		cronQueueService = new CronQueueService(redisQueue, redis, timeDealService);
	}

	@AfterEach
	void tearDown() {
		client.getScoredSortedSet(TEST_KEY).clear();
		client.getSet(TEST_PARTICIPANT).clear();
	}


	@Test
	@Transactional
	void 처음_신청한_유저인_경우_쿠폰을_발급_받을_수_있어야_함(){
		String clientId = "1";
		redisQueue.addParticipation(TEST_KEY, TEST_PARTICIPANT, clientId);
		cronQueueService.processParticipationQueue();

		RScoredSortedSet<Object> scoredSortedSet = client.getScoredSortedSet(TEST_KEY);
		Assertions.assertEquals(0, scoredSortedSet.size());
		Assertions.assertEquals(0, client.getSet(TEST_PARTICIPANT).size());
	}
}
