package com.bjcareer.stockservice.timeDeal.domain.event.exception;

public class CouponLimitExceededException extends RuntimeException {
	public CouponLimitExceededException(String message) {
		super(message);
	}
}

