package com.bjcareer.payment.application.port.out;

import java.util.List;

import com.bjcareer.payment.application.domain.entity.coupon.PaymentCoupon;

public interface LoadCouponPort {
	List<PaymentCoupon> getCoupons(List<Long> couponIds);
}
