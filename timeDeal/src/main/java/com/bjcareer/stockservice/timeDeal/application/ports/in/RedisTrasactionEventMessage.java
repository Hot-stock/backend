package com.bjcareer.stockservice.timeDeal.application.ports.in;

import com.bjcareer.stockservice.timeDeal.domain.event.Event;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class RedisTrasactionEventMessage {
	private final Event event;
}
