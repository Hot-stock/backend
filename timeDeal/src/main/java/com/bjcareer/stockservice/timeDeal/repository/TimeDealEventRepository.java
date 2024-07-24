package com.bjcareer.stockservice.timeDeal.repository;


import com.bjcareer.stockservice.timeDeal.domain.Coupon;
import com.bjcareer.stockservice.timeDeal.domain.TimeDealEvent;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class TimeDealEventRepository {
    private final EntityManager em;

    @Transactional
    public void save(TimeDealEvent timeDealEvent) {
        em.persist(timeDealEvent);
    }

    @Transactional
    public TimeDealEvent findById(Long id) {
        return em.find(TimeDealEvent.class, id);
    }
}
