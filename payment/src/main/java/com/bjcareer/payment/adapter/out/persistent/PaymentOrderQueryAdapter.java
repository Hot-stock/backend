package com.bjcareer.payment.adapter.out.persistent;

import static com.bjcareer.payment.adapter.out.persistent.repository.queries.PaymentOrderQuery.*;

import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Repository;

import com.bjcareer.payment.adapter.out.persistent.repository.queries.PaymentOrderQuery;
import com.bjcareer.payment.application.domain.entity.event.PendingPaymentEvent;
import com.bjcareer.payment.application.domain.entity.order.PaymentStatus;
import com.bjcareer.payment.application.port.out.LoadPendingProductPort;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
@RequiredArgsConstructor
public class PaymentOrderQueryAdapter implements LoadPendingProductPort {
	private final DatabaseClient client;


	@Override
	public Flux<PendingPaymentEvent> loadPendingProduct(PaymentStatus status) {
		return client.sql(PaymentOrderQuery.FIND_PENDING_PAYMENT)
			.bind("status", status.toString())  // 바인딩 변수에 값을 넣습니다
			.map((row, rowMetadata) -> {
				PendingPaymentEvent event = new PendingPaymentEvent();
				event.setPaymentEventId(row.get(PAYMENT_EVENT_ID, Long.class));
				event.setPaymentOrderId(row.get(PAYMENT_ORDER_ID, Long.class));
				event.setPaymentKey(row.get(PAYMENT_KEY, String.class));
				event.setCheckoutId(row.get(CHECKOUT_ID, String.class));
				event.setFailCount(row.get(FAIL_COUNT, Integer.class));
				event.setThreshold(row.get(THRESHOLD, Integer.class));

				String statusStr = row.get(PAYMENT_ORDER_STATUS, String.class);
				event.setStatus(PaymentStatus.valueOf(statusStr));  // 문자열을 ENUM으로 변환

				return event;
			})
			.all();  // 결과를 Flux로 반환
	}

	@Override
	public Mono<Long> loadAmountOfPendingPayment(Long paymentEventId) {
		return client.sql(PaymentOrderQuery.TOTAL_AMOUNT_OF_PAYMENT_EVENT_ID)
			.bind(PAYMENT_EVENT_ID, paymentEventId)
			.map((row, rowMetadata) -> row.get(TOTAL_AMOUNT, Long.class))
			.one();
	}

}
