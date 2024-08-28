package com.bjcareer.payment.payment.adapter.out.persistent.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import com.bjcareer.payment.payment.adapter.application.port.domain.entity.event.PaymentEvent;

public interface PaymentEventRepository extends ReactiveCrudRepository<PaymentEvent, Long> {
}
