package com.bjcareer.payment.adapter.in.request;

import lombok.Data;

@Data
public class TossPaymentConfirmRequest {
	private String paymentKey;
	private String orderId;
	private Long amount;
}
