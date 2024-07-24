package com.bjcareer.stockservice.timeDeal.repository;

import com.bjcareer.stockservice.timeDeal.domain.TimeDealEvent;
import org.springframework.scheduling.annotation.Async;
import org.springframework.transaction.annotation.Transactional;

public interface EventRepository {
    public Long save(TimeDealEvent timeDealEvent);
    public TimeDealEvent findById(Long id);
    @Async @Transactional
    public void saveAsync(TimeDealEvent timeDealEvent);
}
