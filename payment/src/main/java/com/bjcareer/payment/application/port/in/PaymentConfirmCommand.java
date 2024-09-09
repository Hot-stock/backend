package com.bjcareer.payment.application.port.in;

import lombok.Data;

@Data
public class PaymentConfirmCommand {
	private String paymentKey;
	private String orderId;
	private Long amount;

	public PaymentConfirmCommand(String paymentKey, String orderId, Long amount) {
		this.paymentKey = paymentKey;
		this.orderId = orderId;
		this.amount = amount;
	}
}
