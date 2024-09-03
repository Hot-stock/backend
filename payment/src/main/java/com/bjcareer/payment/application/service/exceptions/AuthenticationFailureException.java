package com.bjcareer.payment.application.service.exceptions;

public class AuthenticationFailureException extends  RuntimeException{
	public AuthenticationFailureException() {
	}

	public AuthenticationFailureException(String message) {
		super(message);
	}

	public AuthenticationFailureException(String message, Throwable cause) {
		super(message, cause);
	}

	public AuthenticationFailureException(Throwable cause) {
		super(cause);
	}

	public AuthenticationFailureException(String message, Throwable cause, boolean enableSuppression,
		boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
