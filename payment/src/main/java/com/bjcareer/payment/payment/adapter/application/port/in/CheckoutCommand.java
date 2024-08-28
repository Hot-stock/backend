package com.bjcareer.payment.payment.adapter.application.port.in;

import java.util.List;

import com.bjcareer.payment.payment.adapter.IdempotencyCreator;

import lombok.Getter;

@Getter
public class CheckoutCommand {
	private String buyerId;
	private List<Long> productIds;
	private List<Long> couponIds;
	private String idempotenceKey;

	public CheckoutCommand(Long cartId, String buyerId, List<Long> productIds, List<Long> couponIds) {
		this.buyerId = buyerId;
		this.productIds = productIds;
		this.idempotenceKey = IdempotencyCreator.create(cartId);
		this.couponIds = couponIds;
	}
}
