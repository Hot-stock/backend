package com.bjcareer.stockservice.timeDeal.service;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import java.util.concurrent.TimeUnit;

import org.awaitility.Awaitility;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.data.util.Pair;

class CronQueueServiceTest {
	@Mock
	private TimeDealService timeDealService;
	private CronQueueService cronQueueService;

	@BeforeEach
	void setUp() {
		CronQueueService.queueParticipation.clear();

		timeDealService = Mockito.mock(TimeDealService.class);
		cronQueueService = new CronQueueService(timeDealService);
	}

	@Test
	void testGetParticipationInQueue_WithLessThan30Items() {
		// given
		for (long i = 1; i <= 10; i++) {
			CronQueueService.queueParticipation.add(Pair.of(i, "User" + i));
		}

		// when
		cronQueueService.getParticipationInQueue();

		// then
		verify(timeDealService, times(10)).generateCouponToUser(anyLong(), anyString());
	}

	@Test
	void testGetParticipationInQueue_WithMoreThan30Items() {
		// given
		for (long i = 1; i <= 50; i++) {
			CronQueueService.queueParticipation.add(Pair.of(i, "User" + i));
		}

		// when
		cronQueueService.getParticipationInQueue();

		// then
		verify(timeDealService, times(30)).generateCouponToUser(anyLong(), anyString());
	}

	@Test
	void testGetParticipationInQueue_WithEmptyQueue() {
		// given
		// Queue is already empty

		// when
		cronQueueService.getParticipationInQueue();

		// then
		verify(timeDealService, times(0)).generateCouponToUser(anyLong(), anyString());
	}
}