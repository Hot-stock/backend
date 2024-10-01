package com.bjcareer.userservice.application.register;

public class VerifyTokenDoesNotExist extends RuntimeException {
	public VerifyTokenDoesNotExist() {
		super();
	}

	public VerifyTokenDoesNotExist(String message) {
		super(message);
	}

	public VerifyTokenDoesNotExist(String message, Throwable cause) {
		super(message, cause);
	}

	public VerifyTokenDoesNotExist(Throwable cause) {
		super(cause);
	}

	public VerifyTokenDoesNotExist(String message, Throwable cause, boolean enableSuppression,
		boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
