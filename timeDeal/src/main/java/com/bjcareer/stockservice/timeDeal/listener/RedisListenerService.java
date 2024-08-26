package com.bjcareer.stockservice.timeDeal.listener;

import org.redisson.api.listener.PatternMessageListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bjcareer.stockservice.timeDeal.domain.coupon.Coupon;
import com.bjcareer.stockservice.timeDeal.domain.event.Event;
import com.bjcareer.stockservice.timeDeal.repository.CouponRepository;
import com.bjcareer.stockservice.timeDeal.repository.EventRepository;
import com.bjcareer.stockservice.timeDeal.repository.InMemoryEventRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.function.Consumer;

@Slf4j
public class RedisListenerService implements PatternMessageListener<String> {
	private static final String BACKUP_SUFFIX = InMemoryEventRepository.BACKUP;

	private final InMemoryEventRepository memoryEventRepository;
	private final EventRepository eventRepository;
	private final CouponRepository couponRepository;
	private final Map<String, BackupHandler<?>> backupHandlers;

	public RedisListenerService(InMemoryEventRepository memoryEventRepository, EventRepository eventRepository,
		CouponRepository couponRepository) {
		this.memoryEventRepository = memoryEventRepository;
		this.eventRepository = eventRepository;
		this.couponRepository = couponRepository;

		backupHandlers = Map.of(
			InMemoryEventRepository.EVENT_BUCKET, new BackupHandler<>(Event.class, this.eventRepository::save),
			InMemoryEventRepository.COUPON_BUCKET, new BackupHandler<>(Coupon.class, this.couponRepository::save)
		);
	}

	@Override
	@Transactional
	public void onMessage(CharSequence pattern, CharSequence channel, String key) {
		log.info("Received message on pattern: {}, channel: {}, key: {}", pattern, channel, key);

		String[] split = key.split(":");
		String backupKey = key + BACKUP_SUFFIX;

		BackupHandler<?> handler = backupHandlers.get(split[0] + ":");
		if (handler != null) {
			handleBackup(backupKey, handler);
		} else {
			log.warn("No handler found for bucket: {}", split[0]);
		}
	}

	private <T> void handleBackup(String backupKey, BackupHandler<T> handler) {
		memoryEventRepository.findBackupObject(backupKey, handler.getType()).ifPresentOrElse(
			entity -> {
				handler.save(entity);
				memoryEventRepository.deleteKey(backupKey);
				log.info("Successfully persisted and deleted backup for entity: {}", entity);
			},
			() -> log.warn("No backup found for key: {}", backupKey)
		);
	}

	@Getter
	private static class BackupHandler<T> {
		private final Class<T> type;
		private final Consumer<T> saveFunction;

		public BackupHandler(Class<T> type, Consumer<T> saveFunction) {
			this.type = type;
			this.saveFunction = saveFunction;
		}

		public void save(T entity) {
			saveFunction.accept(entity);
		}
	}
}
