package com.bjcareer.stockservice.timeDeal.application.ports.out;

import java.util.List;

import com.bjcareer.stockservice.timeDeal.domain.coupon.OutboxCoupon;

public interface OutboxCouponPort {
	List<OutboxCoupon> load();

	void saveAll(List<OutboxCoupon> outboxCoupons);
}
