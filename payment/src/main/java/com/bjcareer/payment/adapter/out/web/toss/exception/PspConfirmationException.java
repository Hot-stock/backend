package com.bjcareer.payment.adapter.out.web.toss.exception;

public class PspConfirmationException {
	String errorCode;
	String errorMsg;
	boolean isSuccess = false;
	boolean isFailure = false;
	boolean isUnknown = false;

	boolean isRetryAbleError = false;
}
