package com.bjcareer.payment.application.service.exceptions;

public class DuplicatedCheckout extends Exception{
	public DuplicatedCheckout() {
		super();
	}

	public DuplicatedCheckout(String message) {
		super(message);
	}

	public DuplicatedCheckout(String message, Throwable cause) {
		super(message, cause);
	}

	public DuplicatedCheckout(Throwable cause) {
		super(cause);
	}

	protected DuplicatedCheckout(String message, Throwable cause, boolean enableSuppression,
		boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
