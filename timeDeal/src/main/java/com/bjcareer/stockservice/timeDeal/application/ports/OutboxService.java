package com.bjcareer.stockservice.timeDeal.application.ports;

import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bjcareer.stockservice.TopicConfig;
import com.bjcareer.stockservice.timeDeal.application.ports.out.OutboxCouponPort;
import com.bjcareer.stockservice.timeDeal.domain.ParticipationDomain;
import com.bjcareer.stockservice.timeDeal.domain.coupon.OutboxCoupon;
import com.bjcareer.stockservice.timeDeal.service.out.CouponMessageCommand;
import com.bjcareer.stockservice.timeDeal.service.out.MessagePort;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class OutboxService {
	private final OutboxCouponPort port;
	private final MessagePort messagePort;
	private final ObjectMapper mapper = new ObjectMapper();

	private final String failMessage = "쿠폰 발급에 실패했습니다";
	private final String successMessage = "쿠폰 발급에 성공했습니다";

	@Scheduled(fixedRate = 3000)
	@Transactional
	public void processOutboxMessage() throws JsonProcessingException {
		List<OutboxCoupon> load = port.load();

		if (load == null) {
			log.debug("발행할 메세지가 없습니다.");
		}
		log.debug("Outbox message loaded: {}", load);

		for (OutboxCoupon outboxCoupon : load) {
			ParticipationDomain participationDomain = mapper.readValue(outboxCoupon.getPayload(),
				ParticipationDomain.class);

			messagePort.sendCouponMessage(TopicConfig.COUPON_TOPIC,
				new CouponMessageCommand(participationDomain.getSessionId(), participationDomain.isResult(),
					participationDomain.isResult() ? successMessage : failMessage));

			outboxCoupon.delivered();
		}

		port.saveAll(load);
		log.debug("Outbox message processed: {}", load);
	}
}
