package com.bjcareer.stockservice.timeDeal.application.ports;

import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import com.bjcareer.stockservice.timeDeal.application.ports.in.RedisTrasactionEventMessage;
import com.bjcareer.stockservice.timeDeal.repository.InMemoryEventRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RequiredArgsConstructor
public class CacheTransactionEvent {
	public static final long ALIVE_MINUTE = 5L;
	private final InMemoryEventRepository inMemoryEventRepository;

	@TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
	public void transactionEventAfterCommit(RedisTrasactionEventMessage message) {
		log.info("Transaction Event: {}", message);
		inMemoryEventRepository.save(message.getEvent(), CacheTransactionEvent.ALIVE_MINUTE);
	}
}
