package com.bjcareer.stockservice.timeDeal.repository;

import com.bjcareer.stockservice.timeDeal.domain.Coupon;
import com.bjcareer.stockservice.timeDeal.domain.TimeDealEvent;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class CouponRepositoryTest {
    @Autowired EntityManager em;
    @Autowired CouponRepository couponRepository;

    TimeDealEvent timeDealEvent;

    @BeforeEach
    void setUp() {
        timeDealEvent = new TimeDealEvent(100);
    }

    @Test
    void 쿠폰_저장하기() {
        em.persist(timeDealEvent);

        Coupon coupon = new Coupon(20.0, timeDealEvent);
        String saveId = couponRepository.save(coupon);
        Coupon findCoupon = em.find(Coupon.class, saveId);

        assertEquals(findCoupon.getCouponNumber(), coupon.getCouponNumber());
        assertEquals(findCoupon.getId(), saveId);
    }

}