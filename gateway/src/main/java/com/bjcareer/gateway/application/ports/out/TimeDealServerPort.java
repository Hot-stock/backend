package com.bjcareer.gateway.application.ports.out;

import com.bjcareer.gateway.application.ports.in.ParticipateEventCommand;
import com.bjcareer.gateway.domain.ResponseDomain;

public interface TimeDealServerPort {
	ResponseDomain participateEvent(ParticipateEventCommand command);
}
