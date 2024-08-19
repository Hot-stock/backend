package com.bjcareer.stockservice.timeDeal.domain.event.exception;

public class DuplicateParticipationException extends RuntimeException{
	public DuplicateParticipationException(String message) {
		super(message);
	}
}
