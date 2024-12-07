package com.bjcareer.gateway.in.api;

import java.io.IOException;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.bjcareer.gateway.application.ports.in.CouponEventPushUsecase;
import com.bjcareer.gateway.application.ports.in.ParticipateEventCommand;
import com.bjcareer.gateway.application.ports.out.TimeDealServerPort;
import com.bjcareer.gateway.common.CookieHelper;
import com.bjcareer.gateway.common.Logger;
import com.bjcareer.gateway.domain.ResponseDomain;
import com.bjcareer.gateway.out.api.timeDeal.response.ParticipationEventResponseDTO;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v0/time-deal")
@Slf4j
public class TimeDealController {
	private final TimeDealServerPort port;
	private final CouponEventPushUsecase usecase;

	@PostMapping("/tickets/{eventId}")
	@Operation(summary = "eventId에 참여하는 API", description = "eventId에 참여하는 API 결과는 SSE로 전달됨")
	public ResponseEntity<ResponseDomain<ParticipationEventResponseDTO>> participateOfEvnet(
		@PathVariable("eventId") Long eventId, @CookieValue(CookieHelper.SESSION_ID) String sessionId) {
		log.info("participateOfEvnet Request: {} {}", eventId, sessionId);

		ResponseDomain responseDomain = port.participateEvent(new ParticipateEventCommand(eventId, sessionId));
		return new ResponseEntity<>(responseDomain, responseDomain.getStatusCode());
	}

	@GetMapping(value = "/tickets/result", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
	@Operation(summary = "event의 참여에 대한 결과를 응답 받을 수 있는 api", description = "sse방식으로 서버에서 관리하는 단방향 통신임")
	public SseEmitter getResultOfCoupon(@CookieValue(CookieHelper.SESSION_ID) String sessionId) throws IOException {
		log.info("getResultOfCoupon Request: {}", sessionId);
		SseEmitter emitter = new SseEmitter(0L);
		usecase.addCouponEvent(sessionId, emitter);
		log.info("getResultOfCoupon Response: {}", sessionId);
		return emitter;
	}
}
