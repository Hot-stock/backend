package com.bjcareer.stockservice.timeDeal.service;

import static org.mockito.Mockito.*;

import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.bjcareer.stockservice.timeDeal.application.ports.TimeDealService;
import com.bjcareer.stockservice.timeDeal.application.ports.out.OutboxCouponPort;
import com.bjcareer.stockservice.timeDeal.domain.ParticipationDomain;
import com.bjcareer.stockservice.timeDeal.domain.redis.Redis;
import com.bjcareer.stockservice.timeDeal.domain.redis.RedisQueue;

@ExtendWith(MockitoExtension.class)
class CronQueueServiceTest {
	private Long eventId = 1L;
	public String TEST_KEY = TimeDealService.REDIS_QUEUE_NAME + eventId;
	public String TEST_PARTICIPANT = TimeDealService.REDIS_PARTICIPANT_SET;

	@Mock
	private TimeDealService timeDealService;

	@Mock
	private RedisQueue redisQueue;

	@Mock
	private Redis redis;

	@Mock
	private OutboxCouponPort couponPort;

	@InjectMocks
	private CronQueueService cronQueueService;

	@BeforeEach
	void setUp() {
		cronQueueService = new CronQueueService(redisQueue, redis, timeDealService, couponPort);
	}

	@Test
	void 처음_신청한_유저인_경우_쿠폰을_발급_받을_수_있어야_함(){
		String clientId = UUID.randomUUID().toString();
		String sessionId = UUID.randomUUID().toString();

		ParticipationDomain participationDomain = new ParticipationDomain(clientId, sessionId);

		when(redis.getSingleKeyUsingScan(TimeDealService.REDIS_QUEUE_NAME + "*")).thenReturn(TEST_KEY);
		when(redisQueue.getClientInfoUsingBatch(TEST_KEY)).thenReturn(participationDomain).thenReturn(null);
		doNothing().when(timeDealService).validateEventParticipant(any(), any());
		when(timeDealService.updateDeliveryEventCoupon(1L, 1)).thenReturn(0);

		cronQueueService.processParticipationQueue();

		verify(couponPort, times(1)).saveAll(any());
		verify(timeDealService, times(1)).bulkGenerateCoupon(any(), any());
		verify(redisQueue, times(1)).removeParticipation(any());
	}
}
