package com.bjcareer.stockservice.timeDeal.repository.event;


import com.bjcareer.stockservice.timeDeal.domain.event.Event;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UnlockEventRepository implements EventCustomRepository {
    @PersistenceContext
    private final EntityManager em;

    @Override
    public void saveAsync(Event timeDealEvent) {
    }
}
