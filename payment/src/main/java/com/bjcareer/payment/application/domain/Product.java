package com.bjcareer.payment.application.domain;

import lombok.Getter;

@Getter
public class Product {
	private Long id;
	private int price;

	public Product(Long id, int price) {
		this.id = id;
		this.price = price;
	}
}
