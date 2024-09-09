package com.bjcareer.payment.adapter.out.web.toss.exception;

import lombok.Data;

@Data
public class PspConfirmationException extends RuntimeException{
	private String errorCode;
	private String errrMsg;

	private boolean isSuccess = false;
	private boolean isFailure = false;
	private boolean isUnknown = false;
	private boolean isRetryAbleError = false;

	public PspConfirmationException(String errorCode, String errrMsg, TossErrorCode tossErrorCode) {
		super(errrMsg);
		this.errorCode = errorCode;
		this.errrMsg = errrMsg;
		this.isSuccess = tossErrorCode.isSuccess();
		this.isFailure = tossErrorCode.isSFailure();
		this.isUnknown = tossErrorCode.isUnknown();

		this.isRetryAbleError = isUnknown;
	}
}
