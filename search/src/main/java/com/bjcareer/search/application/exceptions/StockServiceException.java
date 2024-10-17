package com.bjcareer.search.application.exceptions;

public class StockServiceException extends RuntimeException {
	public StockServiceException() {
		super();
	}

	public StockServiceException(String message) {
		super(message);
	}

	public StockServiceException(String message, Throwable cause) {
		super(message, cause);
	}

	public StockServiceException(Throwable cause) {
		super(cause);
	}

	protected StockServiceException(String message, Throwable cause, boolean enableSuppression,
		boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
