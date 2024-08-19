package com.bjcareer.stockservice.timeDeal.repository.event;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.bjcareer.stockservice.timeDeal.domain.event.Event;
import com.bjcareer.stockservice.timeDeal.repository.EventRepository;

@Transactional
@SpringBootTest
class EventRepositoryTest {
	@Autowired
	EventRepository eventRepository;
	public static final int PUBLISHED_COUPON_NUM = 100;
	public static final int DISCOUNT_PERCENTAGE = 100;

	@Test
	void save_event(){
		Event event = new Event(PUBLISHED_COUPON_NUM, DISCOUNT_PERCENTAGE);
		eventRepository.save(event);

		assertNotNull(event.getId());
		assertEquals(PUBLISHED_COUPON_NUM, event.getPublishedCouponNum());
	}
}