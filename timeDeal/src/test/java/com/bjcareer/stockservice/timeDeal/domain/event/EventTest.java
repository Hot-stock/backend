package com.bjcareer.stockservice.timeDeal.domain.event;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.bjcareer.stockservice.timeDeal.domain.event.exception.InvalidEventException;

class EventTest {

	private Event event;

	@BeforeEach
	void setUp() {
		// Initialize event before each test
		event = new Event(10, 15);
	}

	@Test
	void shouldReturnExceededCouponCountAndChangeStatusWhenCouponsExceeded() {
		int deliveryCoupon = 15;

		// Request to deliver more coupons than available
		int exceededCouponSize = event.deliverCoupons(deliveryCoupon);

		// Verify the number of exceeded coupons and the event status
		assertEquals(deliveryCoupon - event.getPublishedCouponNum(), exceededCouponSize);
		assertEquals(EventStatus.CLOSED, event.getStatus());
	}

	@Test
	void shouldThrowExceptionWhenValidatingEventStatusAfterClosure() {
		// Deliver all available coupons, causing the event to close
		event.deliverCoupons(100);

		// Verify that an exception is thrown when validating the status of a closed event
		assertThrows(InvalidEventException.class, event::validateEventStatus, "Should throw InvalidEventException when event is closed and status is checked");
	}
}
