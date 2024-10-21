package com.bjcareer.stockservice.timeDeal.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.redisson.api.RScoredSortedSet;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.bjcareer.stockservice.timeDeal.application.ports.TimeDealService;
import com.bjcareer.stockservice.timeDeal.application.ports.out.OutboxCouponPort;
import com.bjcareer.stockservice.timeDeal.domain.ParticipationDomain;
import com.bjcareer.stockservice.timeDeal.domain.coupon.OutboxCoupon;
import com.bjcareer.stockservice.timeDeal.domain.event.exception.InvalidEventException;
import com.bjcareer.stockservice.timeDeal.domain.redis.Redis;
import com.bjcareer.stockservice.timeDeal.domain.redis.RedisQueue;
import com.bjcareer.stockservice.timeDeal.service.exception.RedisLockAcquisitionException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class CronQueueService {

	private static final int INITIAL_CAPACITY = 1000;
	private final RedisQueue redisQueue;
	private final Redis redis;
	private final TimeDealService timeDealService;
	private final OutboxCouponPort outboxCouponPort;

	@Scheduled(cron = "0/10 * * * * *")
	public void processParticipationQueue() {
		log.debug("Cron job started: processParticipationQueue");

		String key = redis.getSingleKeyUsingScan(TimeDealService.REDIS_QUEUE_NAME + "*");
		if (key == null) {
			log.debug("No matching queue found.");
			return;
		}

		Map<String, ParticipationDomain> participations = extractParticipationFromPQ(key);
		Long eventId = extractEventIdFromKey(key);

		timeDealService.validateEventParticipant(eventId, participations);
		Optional<Integer> excessCoupons = updateEventStatus(eventId, participations, key);

		if (excessCoupons.isEmpty()) {
			outboxCouponPort.saveAll(getOutboxCoupons(participations));
			return;
		}

		List<ParticipationDomain> validParticipants = calculateValidCouponParticipants(participations,
			excessCoupons.get());

		timeDealService.bulkGenerateCoupon(eventId, validParticipants);
		outboxCouponPort.saveAll(getOutboxCoupons(participations));
		redisQueue.removeParticipation(key);
	}

	private static List<OutboxCoupon> getOutboxCoupons(Map<String, ParticipationDomain> participations) {
		ObjectMapper objectMapper = new ObjectMapper();
		List<OutboxCoupon> messages = participations.values().stream().map(participation -> {
			try {
				String payload = objectMapper.writeValueAsString(participation);
				return new OutboxCoupon(payload, participation.isResult());
			} catch (JsonProcessingException e) {
				throw new RuntimeException(e);
			}
		}).collect(Collectors.toList());
		return messages;
	}

	private Map<String, ParticipationDomain> extractParticipationFromPQ(String key) {
		Map<String, ParticipationDomain> participations = new HashMap<>(INITIAL_CAPACITY);

		for (int i = 0; i < INITIAL_CAPACITY; i++) {
			ParticipationDomain participationDomain = redisQueue.getClientInfoUsingBatch(key);

			if (participationDomain == null) {
				break;
			}

			participations.put(participationDomain.getClientId(), participationDomain);
		}

		return participations;
	}

	private static List<ParticipationDomain> calculateValidCouponParticipants(
		Map<String, ParticipationDomain> participations,
		Integer excessCoupons) {
		List<ParticipationDomain> validParticipants = new ArrayList<>(participations.values());

		if (excessCoupons > 0) {
			int validParticipantsCount = participations.size() - excessCoupons;
			validParticipants = validParticipants.subList(0, validParticipantsCount);
		}

		return validParticipants;
	}

	private Optional<Integer> updateEventStatus(Long eventId, Map<String, ParticipationDomain> participations,
		String key) {
		RScoredSortedSet<ParticipationDomain> pq = redisQueue.getScoredSortedSet(key);

		try {
			return Optional.of(timeDealService.updateDeliveryEventCoupon(eventId, participations.size()));
		} catch (RedisLockAcquisitionException e) {
			log.error("Error updating event status: {}", e.getMessage(), e);
			restoreParticipationQueue(participations, pq);
		} catch (InvalidEventException e) {
			log.error("Invalid event exception: {}", e.getMessage(), e);
		}
		return Optional.empty();
	}

	private void restoreParticipationQueue(Map<String, ParticipationDomain> participationScores,
		RScoredSortedSet<ParticipationDomain> pq) {
		participationScores.forEach((key, value) -> pq.add(value.getParticipationIndex(), value));
	}

	private Long extractEventIdFromKey(String key) {
		String[] keyParts = key.split(":");
		return Long.parseLong(keyParts[2]);
	}
}
