package com.bjcareer.stockservice.timeDeal.domain.event;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class EventTest {

	@Test
	void incrementDeliveredCouponIfPossible() {
		Event event = new Event(10, 15);
		boolean b = event.incrementDeliveredCouponIfPossible();
		assertTrue(b);

		event = new Event(0, 15);
		b = event.incrementDeliveredCouponIfPossible();
		assertFalse(b);
	}
}