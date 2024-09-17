package com.bjcareer.payment.adapter.out.web.psp.exceptions;

import org.springframework.http.HttpStatusCode;

import com.bjcareer.payment.adapter.out.web.psp.toss.exception.TossErrorCode;
import com.bjcareer.payment.application.domain.entity.order.PaymentStatus;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Data
@Slf4j
public class PspConfirmationException extends RuntimeException{
	private HttpStatusCode errorCode;
	private String errrMsg;

	private boolean isSuccess;
	private boolean isFailure;
	private boolean isUnknown;
	private boolean isRetryAbleError ;

	public PspConfirmationException(HttpStatusCode errorCode, String errrMsg, HotStockPspErrorCode hotStockPspErrorCode) {
		super(hotStockPspErrorCode.getMessage());
		this.errorCode = errorCode;
		this.errrMsg = hotStockPspErrorCode.getMessage();
		this.isSuccess = hotStockPspErrorCode.isSuccess();
		this.isFailure = hotStockPspErrorCode.isFailure();
		this.isUnknown = hotStockPspErrorCode.isUnknown();;

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
