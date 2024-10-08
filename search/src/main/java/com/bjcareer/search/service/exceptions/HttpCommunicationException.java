package com.bjcareer.search.service.exceptions;

public class HttpCommunicationException extends RuntimeException {
	public HttpCommunicationException() {
	}

	public HttpCommunicationException(String message) {
		super(message);
	}

	public HttpCommunicationException(String message, Throwable cause) {
		super(message, cause);
	}

	public HttpCommunicationException(Throwable cause) {
		super(cause);
	}

	protected HttpCommunicationException(String message, Throwable cause, boolean enableSuppression,
		boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
