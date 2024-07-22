package com.bjcareer.stockservice.timeDeal.repository.event;


import com.bjcareer.stockservice.timeDeal.domain.TimeDealEvent;
import com.bjcareer.stockservice.timeDeal.repository.EventRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
public class UnlockEventRepository implements EventRepository {
    @PersistenceContext
    private final EntityManager em;

    public Long save(TimeDealEvent timeDealEvent) {
        em.persist(timeDealEvent);
        return timeDealEvent.getId();
    }
    public TimeDealEvent findById(Long id) {
        return em.find(TimeDealEvent.class, id);
    }

}
