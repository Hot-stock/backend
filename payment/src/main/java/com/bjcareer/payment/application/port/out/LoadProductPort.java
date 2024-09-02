package com.bjcareer.payment.application.port.out;

import java.util.List;

import com.bjcareer.payment.application.domain.Product;

public interface LoadProductPort {
	List<Product> getProducts(List<Long> productIds);
}
