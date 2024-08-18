package com.bjcareer.stockservice.timeDeal.repository.coupon;

import org.springframework.scheduling.annotation.Async;
import org.springframework.transaction.annotation.Transactional;

import com.bjcareer.stockservice.timeDeal.domain.coupon.Coupon;

public interface CustomCouponRepository {
	@Async
	@Transactional
	public void saveAsync(Coupon coupon);
}
