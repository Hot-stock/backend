package com.bjcareer.payment.payment.adapter.application.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.bjcareer.payment.payment.adapter.application.port.domain.CheckoutResult;
import com.bjcareer.payment.payment.adapter.application.port.domain.Product;
import com.bjcareer.payment.payment.adapter.application.port.domain.entity.coupon.PaymentCoupon;
import com.bjcareer.payment.payment.adapter.application.port.domain.entity.event.PaymentEvent;
import com.bjcareer.payment.payment.adapter.application.port.domain.entity.order.PaymentOrder;
import com.bjcareer.payment.payment.adapter.application.port.in.CheckoutCommand;
import com.bjcareer.payment.payment.adapter.application.port.in.CheckoutUsecase;
import com.bjcareer.payment.payment.adapter.application.port.out.LoadCouponPort;
import com.bjcareer.payment.payment.adapter.application.port.out.LoadProductPort;
import com.bjcareer.payment.payment.adapter.application.port.out.SavePaymentPort;
import com.bjcareer.payment.payment.adapter.application.service.excpetions.CheckoutFailedException;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class CheckoutService implements CheckoutUsecase {
	private final LoadProductPort loadProductPort;
	private final LoadCouponPort loadCouponPort;
	private final SavePaymentPort savePaymentPort;

	@Override
	public Mono<CheckoutResult> checkout(CheckoutCommand checkoutCommand) {
		List<Product> products = loadProducts(checkoutCommand);
		List<PaymentCoupon> coupons = loadCoupons(checkoutCommand);

		PaymentEvent paymentEvent = createPaymentEvent(checkoutCommand, products, coupons);
		return savePaymentPort.save(paymentEvent).flatMap(this::toCheckoutResult).onErrorMap(e -> new CheckoutFailedException("Checkout failed due to a database error.", e));
	}

	private List<Product> loadProducts(CheckoutCommand checkoutCommand) {
		return loadProductPort.getProducts(checkoutCommand.getProductIds());
	}

	private List<PaymentCoupon> loadCoupons(CheckoutCommand checkoutCommand) {
		return loadCouponPort.getCoupons(checkoutCommand.getCouponIds());
	}

	private PaymentEvent createPaymentEvent(CheckoutCommand checkoutCommand, List<Product> products, List<PaymentCoupon> coupons) {
		List<PaymentOrder> paymentOrders = products.stream()
			.map(product -> toPaymentOrder(product))
			.collect(Collectors.toList());

		return new PaymentEvent(checkoutCommand.getBuyerId(), checkoutCommand.getIdempotenceKey(), paymentOrders, coupons);
	}

	private PaymentOrder toPaymentOrder(Product product) {
		return new PaymentOrder(product.getId());
	}

	private Mono<CheckoutResult> toCheckoutResult(PaymentEvent paymentEvent) {
		System.out.println("paymentEvent.getTotalAmount() = " + paymentEvent.getTotalAmount());
		return Mono.just(new CheckoutResult(paymentEvent.getTotalAmount(), paymentEvent.getOrderId()));
	}
}
