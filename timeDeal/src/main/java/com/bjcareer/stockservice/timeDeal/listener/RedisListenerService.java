package com.bjcareer.stockservice.timeDeal.listener;


import org.redisson.api.listener.PatternMessageListener;
import org.springframework.stereotype.Service;

import com.bjcareer.stockservice.timeDeal.repository.EventRepository;
import com.bjcareer.stockservice.timeDeal.repository.InMemoryEventRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class RedisListenerService implements PatternMessageListener<String> {

	private static final String EVENT_PREFIX = "EVENT";
	private static final String BACKUP_SUFFIX = InMemoryEventRepository.BACKUP;

	private final InMemoryEventRepository memoryEventRepository;
	private final EventRepository eventRepository;

	@Override
	public void onMessage(CharSequence pattern, CharSequence channel, String key) {
		log.info("Received message on pattern: {}, channel: {}, key: {}", pattern, channel, key);

		String[] split = key.split(":");

		if (!EVENT_PREFIX.equals(split[0])) {
			log.warn("Invalid key format or not an EVENT key: {}", key);
			return;
		}

		String backupKey = key + BACKUP_SUFFIX;

		memoryEventRepository.findBackupObject(backupKey).ifPresentOrElse(
			event -> {
				eventRepository.save(event);
				memoryEventRepository.deleteKey(backupKey);
				log.info("Successfully persisted and deleted backup for event: {}", event);
			},
			() -> log.warn("No backup found for key: {}", backupKey)
		);
	}
}
