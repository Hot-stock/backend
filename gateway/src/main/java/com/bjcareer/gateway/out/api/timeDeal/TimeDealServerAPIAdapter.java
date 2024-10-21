package com.bjcareer.gateway.out.api.timeDeal;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;

import com.bjcareer.gateway.application.ports.in.ParticipateEventCommand;
import com.bjcareer.gateway.application.ports.out.TimeDealServerPort;
import com.bjcareer.gateway.common.CookieHelper;
import com.bjcareer.gateway.common.Logger;
import com.bjcareer.gateway.domain.ErrorDomain;
import com.bjcareer.gateway.domain.ResponseDomain;

@Component
public class TimeDealServerAPIAdapter implements TimeDealServerPort {
	private final WebClient webClient;
	private final Logger log;

	public TimeDealServerAPIAdapter(@Qualifier("timeDealWebClient") WebClient webClient, Logger logger) {
		this.webClient = webClient;
		this.log = logger;
	}

	public ResponseDomain participateEvent(ParticipateEventCommand command) {
		ClientResponse response = webClient.post()
			.uri(TimeDealServerURI.PARTICIPATE_EVENT + command.getEventId())
			.cookies(c -> c.add(CookieHelper.SESSION_ID, command.getSessionId()))
			.exchange()
			.block();

		log.info("Response status of TimeDeal Server for participateEvent Request: {}", response.statusCode());

		if (!response.statusCode().is2xxSuccessful()) {
			ErrorDomain error = response.bodyToMono(ErrorDomain.class).block();
			log.error("Error response from TimeDeal Server for participateEvent Request: {}", error.getMessage());
			throw new RuntimeException(error.getMessage());
		}

		return new ResponseDomain<>(response.statusCode(), response.bodyToMono(ParticipateEventCommand.class), null);
	}
}
