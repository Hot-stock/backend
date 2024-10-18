package com.bjcareer.stockservice.timeDeal.application.ports.in;

import com.bjcareer.stockservice.timeDeal.domain.event.Event;

public interface CreateEventUsecase {
	Event createEvent(CreateEventCommand command);
}
