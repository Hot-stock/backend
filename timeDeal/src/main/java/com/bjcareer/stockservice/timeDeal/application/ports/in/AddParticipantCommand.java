package com.bjcareer.stockservice.timeDeal.application.ports.in;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class AddParticipantCommand {
	private final String sessionId;
	private final Long eventId;
}
