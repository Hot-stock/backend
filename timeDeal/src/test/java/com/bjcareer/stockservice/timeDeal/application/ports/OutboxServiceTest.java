package com.bjcareer.stockservice.timeDeal.application.ports;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.bjcareer.stockservice.TopicConfig;
import com.bjcareer.stockservice.timeDeal.application.ports.out.OutboxCouponPort;
import com.bjcareer.stockservice.timeDeal.domain.ParticipationDomain;
import com.bjcareer.stockservice.timeDeal.domain.coupon.OutboxCoupon;
import com.bjcareer.stockservice.timeDeal.service.out.CouponMessageCommand;
import com.bjcareer.stockservice.timeDeal.service.out.MessagePort;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@ExtendWith(MockitoExtension.class)
class OutboxServiceTest {

	@Mock
	private OutboxCouponPort port;

	@Mock
	private MessagePort messagePort;

	@InjectMocks
	private OutboxService outboxService;

	private final ObjectMapper mapper = new ObjectMapper();

	@BeforeEach
	void setUp() {
		outboxService = new OutboxService(port, messagePort);
	}

	@Test
	void 에러가_없으면_아웃박스가_모두_true가_됨() throws JsonProcessingException {
		// Arrange
		List<OutboxCoupon> outboxCoupons = createOutboxCoupons();

		when(port.load()).thenReturn(outboxCoupons);

		// Act
		outboxService.processOutboxMessage();

		// Assert
		verify(port, times(1)).saveAll(any());
		assertAllOutboxCouponsDelivered(outboxCoupons);
	}

	@Test
	void processOutboxMessage_예외발생시_트랜잭션_롤백() throws JsonProcessingException {
		// Arrange
		ParticipationDomain participationDomain = new ParticipationDomain(UUID.randomUUID().toString(),
			UUID.randomUUID().toString());
		OutboxCoupon outboxCoupon = new OutboxCoupon(mapper.writeValueAsString(participationDomain), false);
		List<OutboxCoupon> outboxCoupons = List.of(outboxCoupon);

		when(port.load()).thenReturn(outboxCoupons);
		doThrow(new RuntimeException("메시지 전송 중 오류 발생")).when(messagePort).sendCouponMessage(
			eq(TopicConfig.COUPON_TOPIC),
			any(CouponMessageCommand.class)
		);

		// Act & Assert
		assertThrows(RuntimeException.class, () -> outboxService.processOutboxMessage());

		// Verify that saveAll was never called due to rollback
		verify(port, never()).saveAll(any());
		assertFalse(outboxCoupon.isDelivered(), "OutboxCoupon should not be marked as delivered due to rollback");
	}

	private List<OutboxCoupon> createOutboxCoupons() throws JsonProcessingException {
		ParticipationDomain domain1 = new ParticipationDomain(UUID.randomUUID().toString(),
			UUID.randomUUID().toString());
		ParticipationDomain domain2 = new ParticipationDomain(UUID.randomUUID().toString(),
			UUID.randomUUID().toString());

		OutboxCoupon coupon1 = new OutboxCoupon(mapper.writeValueAsString(domain1), false);
		OutboxCoupon coupon2 = new OutboxCoupon(mapper.writeValueAsString(domain2), true);

		return List.of(coupon1, coupon2);
	}

	private void assertAllOutboxCouponsDelivered(List<OutboxCoupon> outboxCoupons) {
		outboxCoupons.forEach(coupon -> assertTrue(coupon.isDelivered(), "OutboxCoupon should be delivered"));
	}
}
