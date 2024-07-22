package com.bjcareer.stockservice.timeDeal.repository;

import com.bjcareer.stockservice.timeDeal.domain.TimeDealEvent;

public interface EventRepository {
    public Long save(TimeDealEvent timeDealEvent);
    public TimeDealEvent findById(Long id);
}
