package com.bjcareer.gateway.application.ports.in;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

public interface CouponEventPushUsecase {
	void addCouponEvent(String sessionId, SseEmitter emitter);
	void sendEvent(String sessionId, String data);
}
