package com.bjcareer.stockservice.timeDeal.out.message;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import com.bjcareer.stockservice.timeDeal.service.out.MessagePort;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class KafkaMessageAdapter implements MessagePort {
	private final KafkaTemplate<String, Boolean> kafkaTemplate;

	@Override
	public void sendCouponMessage(String topic, Boolean result) {
		kafkaTemplate.send(topic, result).thenApply(sendResult -> {
			log.info("Send result: {}", sendResult);
			return sendResult;
		}).exceptionally(e -> {
			log.error("Send failed: {}", e.getMessage());
			return null;
		});
	}
}
