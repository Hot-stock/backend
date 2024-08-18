package com.bjcareer.stockservice.timeDeal.repository;

import com.bjcareer.stockservice.timeDeal.domain.event.Event;
import com.bjcareer.stockservice.timeDeal.repository.event.EventCustomRepository;

import org.springframework.data.repository.CrudRepository;

public interface EventRepository extends CrudRepository<Event, Long>, EventCustomRepository {
}
