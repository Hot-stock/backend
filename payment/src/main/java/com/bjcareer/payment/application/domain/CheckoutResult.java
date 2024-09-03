package com.bjcareer.payment.application.domain;

import lombok.Data;
import lombok.Getter;

@Getter
@Data
public class CheckoutResult {
	private final String amount;
	private final String checkoutId;
	private final String buyerId;
	private final String orderName;

	public CheckoutResult(Long amount, String checkoutId, String buyerId, String orderName) {
		this.amount = String.valueOf(amount);
		this.orderName = orderName;
		this.checkoutId = checkoutId;
		this.buyerId = buyerId;
	}
}
