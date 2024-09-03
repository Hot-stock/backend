package com.bjcareer.payment.adapter.out.web.product;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.bjcareer.payment.application.domain.Product;
import com.bjcareer.payment.application.port.out.LoadProductPort;

@Component
public class ProductWebAdapter implements LoadProductPort {
	public static final int MOCK_PRICE = 2000;

	@Override
	public List<Product> getProducts(List<Long> productIds) {
		//mock 데이터 반환 추후에 msa 연동 과정을 거침
		List<Product> products = new ArrayList<>();

		for (Long productId : productIds) {
			products.add(new Product(productId, MOCK_PRICE));
		}

		return products;

	}
}
