package com.bjcareer.payment.adapter.out.persistent;

import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Repository;

import com.bjcareer.payment.adapter.out.persistent.repository.PaymentOrderRepository;
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
	private final String findPendingPayment = "SELECT " +
		"pe.payment_event_id AS payment_event_id, " +
		"pe.payment_key AS payment_key, " +
		"pe.checkout_id AS checkout_id, " +
		"po.payment_order_id AS payment_order_id, " +
		"po.payment_order_status AS payment_order_status, " +
		"po.fail_count AS fail_count, " +
		"po.threshold AS threshold " +
		"FROM payment_event pe " +
		"INNER JOIN payment_order po ON pe.payment_event_id = po.payment_event_id " +
		"WHERE po.payment_order_status = :status";

	private final String totalAmountSQL = "SELECT " +
		"SUM(po.amount) AS totalAmount " +
		"FROM payment_order po " +
		"WHERE po.payment_event_id = :payment_event_id " +
		"GROUP BY po.payment_event_id";



	@Override
	public Flux<PendingPaymentEvent> loadPendingProduct(PaymentStatus status) {
		return client.sql(findPendingPayment)
			.bind("status", status.toString())  // 바인딩 변수에 값을 넣습니다
			.map((row, rowMetadata) -> {
				PendingPaymentEvent event = new PendingPaymentEvent();
				event.setPaymentEventId(row.get("payment_event_id", Long.class));
				event.setPaymentOrderId(row.get("payment_order_id", Long.class));
				event.setPaymentKey(row.get("payment_key", String.class));
				event.setCheckoutId(row.get("checkout_id", String.class));
				event.setFailCount(row.get("fail_count", Integer.class));
				event.setThreshold(row.get("threshold", Integer.class));

				String statusStr = row.get("payment_order_status", String.class);
				event.setStatus(PaymentStatus.valueOf(statusStr));  // 문자열을 ENUM으로 변환

				return event;
			})
			.all();  // 결과를 Flux로 반환
	}

	@Override
	public Mono<Long> loadAmountOfPendingPayment(Long paymentEventId) {
		return client.sql(totalAmountSQL)
			.bind("payment_event_id", paymentEventId)
			.map((row, rowMetadata) -> row.get("totalAmount", Long.class))
			.one();
	}

}
