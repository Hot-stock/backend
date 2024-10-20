package com.bjcareer.gateway.application.ports;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.bjcareer.gateway.application.ports.in.CouponEventPushUsecase;
import com.bjcareer.gateway.common.Logger;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class CouponEventPushService implements CouponEventPushUsecase {
	private final Map<String, SseEmitter> emitters = new HashMap<>();

	@Override
	public void addCouponEvent(String sessionId, SseEmitter emitter) {
		log.info("addCouponEvent sessionId: {}", sessionId);
		emitters.put(sessionId, emitter);
	}

	@Override
	public void sendEvent(String sessionId, String data) {
		SseEmitter sseEmitter = emitters.get(sessionId);
		if (sseEmitter != null) {
			System.out.println("sseEmitter = " + sseEmitter);
			//("sendEvent sessionId: {} data: {}", sessionId, data);
			try {
				sseEmitter.send(data);
				sseEmitter.complete();
			} catch (Exception e) {
				emitters.remove(sessionId);
			}
		}else{
			System.out.println("sseEmitter = " + sseEmitter);
			//("Fail get session: {} data: {}", sessionId, data);
		}
	}
}
