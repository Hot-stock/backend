package com.bjcareer.stockservice.timeDeal.domain.coupon;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.bjcareer.stockservice.timeDeal.domain.ParticipationDomain;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.persistence.EntityManager;

@SpringBootTest
class OutboxCouponTest {
	public static final String CLIENT_ID = "test";
	public static final String SESSION_ID = "test";
	@Autowired EntityManager em;

	ParticipationDomain participation;
	@BeforeEach
	void setUp() {
		participation = new ParticipationDomain(CLIENT_ID, SESSION_ID);
	}

	@Test
	@Transactional
	void outbox_message_저장_테스트() throws JsonProcessingException {
		ObjectMapper objectMapper = new ObjectMapper();
		String payload = objectMapper.writeValueAsString(participation);
		OutboxCoupon outboxCoupon = new OutboxCoupon(payload, false);
		em.persist(outboxCoupon);
		assertNotNull(outboxCoupon.getId());
	}

	@Test
	@Transactional
	void outbox_message_조회_테스트() throws JsonProcessingException {
		ObjectMapper objectMapper = new ObjectMapper();
		String payload = objectMapper.writeValueAsString(participation);
		OutboxCoupon outboxCoupon = new OutboxCoupon(payload, false);
		em.persist(outboxCoupon);

		OutboxCoupon outboxCoupon1 = em.find(OutboxCoupon.class, outboxCoupon.getId());
		ParticipationDomain participationDomain = objectMapper.readValue(outboxCoupon1.getPayload(),
			ParticipationDomain.class);
		System.out.println(outboxCoupon.getPayload());

		assertEquals("test", participationDomain.getClientId());
		assertEquals("test", participationDomain.getSessionId());
	}

}
