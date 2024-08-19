package com.bjcareer.stockservice.timeDeal.domain.event.exception;

public class InvalidEventException extends RuntimeException{
	public InvalidEventException(String message) {
		super(message);
	}
}
