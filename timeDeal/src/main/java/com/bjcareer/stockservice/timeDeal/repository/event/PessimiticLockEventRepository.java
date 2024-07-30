package com.bjcareer.stockservice.timeDeal.repository.event;

import com.bjcareer.stockservice.timeDeal.domain.TimeDealEvent;
import com.bjcareer.stockservice.timeDeal.repository.EventRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;

@RequiredArgsConstructor
@Slf4j
public class PessimiticLockEventRepository implements EventRepository {
    @PersistenceContext
    private final EntityManager em;

    @Override
    public Long save(TimeDealEvent timeDealEvent) {
        em.persist(timeDealEvent);
        return timeDealEvent.getId();
    }

    @Override
    public TimeDealEvent findById(Long id) {
        TimeDealEvent timeDealEvent = em.find(TimeDealEvent.class, id, LockModeType.PESSIMISTIC_WRITE);
        log.debug("비관적 락 시작 컬럼 pk {}", id);
        return timeDealEvent;
    }

    public void saveAsync(TimeDealEvent timeDealEvent) {
        em.merge(timeDealEvent);
    }
}
