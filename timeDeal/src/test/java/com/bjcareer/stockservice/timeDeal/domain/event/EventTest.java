package com.bjcareer.stockservice.timeDeal.domain.event;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.bjcareer.stockservice.timeDeal.domain.event.exception.CouponLimitExceededException;
import com.bjcareer.stockservice.timeDeal.domain.event.exception.InvalidEventException;

class EventTest {
	private Event event;

	@Test
	void shouldIncrementDeliveredCouponWhenPossible() {
		event = new Event(10, 15);
		assertDoesNotThrow(event::incrementDeliveredCouponIfPossible, "Should not throw an exception when incrementing delivered coupon is possible");

		event = new Event(0, 15);
		assertThrows(CouponLimitExceededException.class, event::incrementDeliveredCouponIfPossible, "Should throw CouponLimitExceededException when no coupons are available");
	}

	@Test
	void shouldThrowExceptionWhenEventIsClosedAndCheckStatusIsCalled() {
		event = new Event(10, 15);
		event.closeEvent();
		assertThrows(InvalidEventException.class, event::checkEventStatus, "Should throw InvalidEventException when event is closed and status is checked");
	}
}
