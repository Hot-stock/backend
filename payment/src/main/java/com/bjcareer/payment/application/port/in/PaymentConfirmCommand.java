package com.bjcareer.payment.application.port.in;

import lombok.Data;

@Data
public class PaymentConfirmCommand {
	private String paymentKey;
	private String checkoutId;
	private Long amount;

	public PaymentConfirmCommand(String paymentKey, String checkoutId, Long amount) {
		this.paymentKey = paymentKey;
		this.checkoutId = checkoutId;
		this.amount = amount;
	}
}
