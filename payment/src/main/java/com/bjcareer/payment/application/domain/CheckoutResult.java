package com.bjcareer.payment.application.domain;

import lombok.Data;
import lombok.Getter;

@Getter
@Data
public class CheckoutResult {
	private final String amount;
	private final String orderId;
	private final String buyerId;
	private final String orderName;

	public CheckoutResult(int amount, String orderId, String buyerId, String orderName) {
		this.amount = String.valueOf(amount);
		this.orderName = orderName;
		this.orderId = orderId;
		this.buyerId = buyerId;
	}
}
