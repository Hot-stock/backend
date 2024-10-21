package com.bjcareer.stockservice.timeDeal.out.persistence;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.bjcareer.stockservice.timeDeal.domain.coupon.OutboxCoupon;

public interface OutBoxCouponRepository extends JpaRepository<OutboxCoupon, Long> {
	@Query("SELECT o FROM OutboxCoupon as o WHERE o.isDelivered = false")
	List<OutboxCoupon> findIsNotDelivered();
}
