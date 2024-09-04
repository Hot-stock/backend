package com.bjcareer.payment.adapter.out.web.toss.exception;

import com.bjcareer.payment.application.domain.entity.order.PaymentStatus;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Data
@Slf4j
public class PspConfirmationException extends RuntimeException{
	private String errorCode;
	private String errrMsg;

	private boolean isSuccess;
	private boolean isFailure;
	private boolean isUnknown;
	private boolean isRetryAbleError ;

	public PspConfirmationException(String errorCode, String errrMsg, TossErrorCode tossErrorCode) {
		super(errrMsg);
		this.errorCode = errorCode;
		this.errrMsg = errrMsg;
		this.isSuccess = tossErrorCode.isSuccess();
		this.isFailure = tossErrorCode.isSFailure();
		this.isUnknown = tossErrorCode.isUnknown();;

		this.isRetryAbleError = isUnknown;
	}

	public PaymentStatus getPaymentStatus(){
		if (isSuccess){
			return PaymentStatus.SUCCESS;
		}else if (isFailure){
			return PaymentStatus.FAILURE;
		}
		return PaymentStatus.UNKNOWN;
	}
}
