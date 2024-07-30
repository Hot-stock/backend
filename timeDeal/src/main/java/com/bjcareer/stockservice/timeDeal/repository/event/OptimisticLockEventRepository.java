package com.bjcareer.stockservice.timeDeal.repository.event;

import com.bjcareer.stockservice.timeDeal.domain.TimeDealEvent;
import com.bjcareer.stockservice.timeDeal.repository.EventRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class OptimisticLockEventRepository implements EventRepository {
    @PersistenceContext
    private final EntityManager em;

    public Long save(TimeDealEvent timeDealEvent) {
        em.persist(timeDealEvent);
        return timeDealEvent.getId();
    }

    public TimeDealEvent findById(Long id) {
        return em.find(TimeDealEvent.class, id, LockModeType.OPTIMISTIC);
    }

    @Override
    public void saveAsync(TimeDealEvent timeDealEvent) {
        em.persist(timeDealEvent);
    }
}
