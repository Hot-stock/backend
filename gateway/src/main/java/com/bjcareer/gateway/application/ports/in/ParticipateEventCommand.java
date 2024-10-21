package com.bjcareer.gateway.application.ports.in;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class ParticipateEventCommand {
	private final Long eventId;
	private final String sessionId;
}
