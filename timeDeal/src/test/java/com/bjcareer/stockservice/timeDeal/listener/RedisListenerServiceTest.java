package com.bjcareer.stockservice.timeDeal.listener;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;


import static org.mockito.Mockito.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.bjcareer.stockservice.timeDeal.domain.event.Event;
import com.bjcareer.stockservice.timeDeal.repository.InMemoryEventRepository;

@SpringBootTest
class RedisListenerServiceTest {
	@Autowired
	InMemoryEventRepository repository;

	@Test
	void test() throws InterruptedException {
		Event mockEvent = Mockito.mock(Event.class);

		when(mockEvent.getId()).thenReturn(1L);
		when(mockEvent.getPublishedCouponNum()).thenReturn(10);
		when(mockEvent.getDeliveredCouponNum()).thenReturn(10);

		Long save = repository.save(mockEvent, 1L);

		Thread.sleep(5000L);

	}
}