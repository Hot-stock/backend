package com.bjcareer.payment.application.service.exceptions;

public class PaymentValidationException extends RuntimeException{
	public PaymentValidationException() {
		super();
	}

	public PaymentValidationException(String message) {
		super(message);
	}

	public PaymentValidationException(String message, Throwable cause) {
		super(message, cause);
	}

	public PaymentValidationException(Throwable cause) {
		super(cause);
	}

	protected PaymentValidationException(String message, Throwable cause, boolean enableSuppression,
		boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
