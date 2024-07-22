package com.bjcareer.stockservice.timeDeal.repository.coupon;


import com.bjcareer.stockservice.timeDeal.domain.Coupon;
import com.bjcareer.stockservice.timeDeal.repository.CouponRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
public class UnlockCouponRepository implements CouponRepository {
    @PersistenceContext
    private final EntityManager em;

    public String save(Coupon coupon) {
        em.persist(coupon);
        return coupon.getId();
    }

    @Transactional
    public Coupon findById(String id) {
        return em.find(Coupon.class, id);
    }
}
