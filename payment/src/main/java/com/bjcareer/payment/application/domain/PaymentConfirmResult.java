package com.bjcareer.payment.application.domain;

import lombok.Data;
import lombok.Getter;

@Getter
public class PaymentConfirmResult {
	private final String paymentKey;
	private final String orderId;
	private final Long amount;

	public PaymentConfirmResult(String paymentKey, String orderId, Long amount) {
		this.paymentKey = paymentKey;
		this.orderId = orderId;
		this.amount = amount;
	}
}
