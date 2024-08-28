package com.bjcareer.payment.payment.adapter.application.port.out;

import java.util.List;

import com.bjcareer.payment.payment.adapter.application.port.domain.Product;

public interface LoadProductPort {
	List<Product> getProducts(List<Long> productIds);
}
