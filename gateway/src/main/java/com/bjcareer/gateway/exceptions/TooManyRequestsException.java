package com.bjcareer.gateway.exceptions;

public class TooManyRequestsException extends RuntimeException {
	public TooManyRequestsException(String message) {
		super(message);
	}
}
