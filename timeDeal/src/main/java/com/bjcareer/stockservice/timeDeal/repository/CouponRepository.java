package com.bjcareer.stockservice.timeDeal.repository;

import java.util.Optional;

import com.bjcareer.stockservice.timeDeal.domain.coupon.Coupon;

import com.bjcareer.stockservice.timeDeal.repository.coupon.CustomCouponRepository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
public interface CouponRepository extends CrudRepository<Coupon,Long>, CustomCouponRepository {
	@Query("SELECT coupon FROM Coupon coupon WHERE coupon.id = :id")
	Optional<Coupon> findById(@Param("id") String id);


	@Query("SELECT coupon FROM Coupon coupon WHERE coupon.event.id = :eventId AND coupon.userPK = :userPK")
	Optional<Coupon> findByEventIdAndUserId(@Param("eventId") Long eventId, @Param("userPK") String userPK);
}
