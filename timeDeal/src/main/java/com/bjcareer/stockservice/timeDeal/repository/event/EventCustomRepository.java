package com.bjcareer.stockservice.timeDeal.repository.event;

import org.springframework.stereotype.Component;

import com.bjcareer.stockservice.timeDeal.domain.event.Event;

@Component("eventCustom")
public interface EventCustomRepository{
	public void saveAsync(Event timeDealEvent);
}
