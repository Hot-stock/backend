package com.bjcareer.payment.adapter.in.request;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class CheckoutRequest {
	Long cartId;
	String buyerPk;

	List<Long> productIds = new ArrayList<>();
	List<Long> couponIds = new ArrayList<>();
}
