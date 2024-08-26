package com.bjcareer.stockservice.timeDeal.repository.event;

import com.bjcareer.stockservice.timeDeal.domain.event.Event;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
public class PessimiticLockEventRepository implements EventCustomRepository {
    @PersistenceContext
    private final EntityManager em;

    public void saveAsync(Event timeDealEvent) {
        em.merge(timeDealEvent);
    }
}
