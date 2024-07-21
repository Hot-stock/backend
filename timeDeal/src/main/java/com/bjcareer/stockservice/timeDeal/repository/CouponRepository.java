package com.bjcareer.stockservice.timeDeal.repository;


import com.bjcareer.stockservice.timeDeal.domain.Coupon;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CouponRepository {
    private final EntityManager em;

    @Transactional
    public void save(Coupon coupon) {
        em.persist(coupon);
    }

    @Transactional
    public Coupon findById(Long id) {
        return em.find(Coupon.class, id);
    }
}
