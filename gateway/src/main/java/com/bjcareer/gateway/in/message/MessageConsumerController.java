package com.bjcareer.gateway.in.message;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Controller;

import com.bjcareer.gateway.application.ports.in.CouponEventPushUsecase;
import com.bjcareer.gateway.common.Logger;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequiredArgsConstructor
@Slf4j
public class MessageConsumerController {
	private final CouponEventPushUsecase couponEventPushUsecase;

	@KafkaListener(topics = "coupon-topic", groupId = "my-consumer-group")
	public void consume(String message) {
		log.info("Consumed message: {}", message);

		CouponMessageCommand command = CouponMessageCommand.parseStringToObj(message);
		couponEventPushUsecase.sendEvent(command.getSessionId(), command.getMessage());
	}
}


