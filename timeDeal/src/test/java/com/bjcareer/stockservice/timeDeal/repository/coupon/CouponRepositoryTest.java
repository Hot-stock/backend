package com.bjcareer.stockservice.timeDeal.repository.coupon;

import com.bjcareer.stockservice.timeDeal.domain.coupon.Coupon;
import com.bjcareer.stockservice.timeDeal.domain.event.Event;
import com.bjcareer.stockservice.timeDeal.repository.CouponRepository;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class CouponRepositoryTest {

    @Autowired
    private EntityManager em;

    @Autowired
    private CouponRepository couponRepository;

    private Event timeDealEvent;

    @BeforeEach
    void setUp() {
        timeDealEvent = new Event(100, 10);
        em.persist(timeDealEvent);
        em.flush();
        em.clear();
    }

    @Test
    void shouldFindCouponById() {
        // Given
        Coupon coupon = new Coupon(timeDealEvent, "testUser");
        couponRepository.save(coupon);

        // When
        Optional<Coupon> foundCoupon = couponRepository.findById(coupon.getId());

        // Then
        assertTrue(foundCoupon.isPresent(), "Coupon should be found by its ID");
        assertEquals("testUser", foundCoupon.get().getUserPK(), "The user associated with the coupon should be 'testUser'");
    }

    @Test
    void shouldSaveAndFindCouponById() {
        // Given
        Coupon coupon = new Coupon(timeDealEvent, "testUser");
        Coupon savedCoupon = couponRepository.save(coupon);

        // When
        Optional<Coupon> foundCoupon = couponRepository.findById(savedCoupon.getId());

        // Then
        assertTrue(foundCoupon.isPresent(), "Coupon should be found by its ID");
        assertEquals(savedCoupon.getId(), foundCoupon.get().getId(), "The found coupon ID should match the saved coupon ID");
    }

    @Test
    void shouldFindDuplicateParticipation() {
        // Given
        Coupon coupon = new Coupon(timeDealEvent, "testUser");
        couponRepository.save(coupon);

        // When
        Optional<Coupon> foundCoupon = couponRepository.findByEventIdAndUserId(timeDealEvent.getId(), "testUser");

        // Then
        assertTrue(foundCoupon.isPresent(), "Coupon should be found for the event and user");
        assertEquals("testUser", foundCoupon.get().getUserPK(), "The user associated with the found coupon should be 'testUser'");
        assertEquals(timeDealEvent.getId(), foundCoupon.get().getEvent().getId(), "The event ID should match the coupon's event ID");
    }
}
