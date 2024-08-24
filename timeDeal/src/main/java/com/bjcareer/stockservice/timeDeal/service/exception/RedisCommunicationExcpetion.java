package com.bjcareer.stockservice.timeDeal.service.exception;

public class RedisCommunicationExcpetion extends RuntimeException {
	public RedisCommunicationExcpetion(String message) {
		super(message);
	}

	public RedisCommunicationExcpetion(String message, Throwable cause) {
		super(message, cause);
	}
}
