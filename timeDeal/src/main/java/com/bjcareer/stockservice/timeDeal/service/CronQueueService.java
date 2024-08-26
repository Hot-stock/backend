package com.bjcareer.stockservice.timeDeal.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.redisson.api.RScoredSortedSet;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.bjcareer.stockservice.timeDeal.domain.event.exception.InvalidEventException;
import com.bjcareer.stockservice.timeDeal.domain.redis.Redis;
import com.bjcareer.stockservice.timeDeal.domain.redis.RedisQueue;
import com.bjcareer.stockservice.timeDeal.domain.redis.VO.ParticipationVO;
import com.bjcareer.stockservice.timeDeal.service.exception.RedisLockAcquisitionException;

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

	@Scheduled(cron = "0/10 * * * * *")
	public void processParticipationQueue() {
		log.debug("Cron job started: processParticipationQueue");

		String key = redis.getSingleKeyUsingScan(TimeDealService.REDIS_QUEUE_NAME + "*");

		if (key == null) {
			log.debug("No matching queue found.");
			return;
		}

		Map<String, Double> participations = extractParticipationFromPQ(key);
		Long eventId = extractEventIdFromKey(key);

		timeDealService.validateEventParticipant(eventId, participations);
		Optional<Integer> excessCoupons = updateEventStatus(eventId, participations, key);

		if (excessCoupons.isEmpty()) {
			return;
		}

		List<String> validParticipants = calculateValidCouponParticipants(participations, excessCoupons.get());
		timeDealService.bulkGenerateCoupon(eventId, validParticipants);
		redisQueue.removeParticipation(key);
	}

	private Map<String, Double> extractParticipationFromPQ(String key) {
		Map<String, Double> participations = new HashMap<>(INITIAL_CAPACITY);

		for (int i = 0; i < INITIAL_CAPACITY; i++) {
			ParticipationVO participationVO = redisQueue.getClientInfoUsingBatch(key);

			if (participationVO == null || participationVO.getScore() == null) {
				break;
			}
			participations.put(participationVO.getClientId(), participationVO.getScore());
		}

		return participations;
	}

	private static List<String> calculateValidCouponParticipants(Map<String, Double> participations, Integer excessCoupons) {
		List<String> validParticipants = new ArrayList<>(participations.keySet());

		if (excessCoupons > 0) {
			int validParticipantsCount = participations.size() - excessCoupons;
			validParticipants = validParticipants.subList(0, validParticipantsCount);
		}

		return validParticipants;
	}

	private Optional<Integer> updateEventStatus(Long eventId, Map<String, Double> participations, String key) {
		RScoredSortedSet<String> pq = redisQueue.getScoredSortedSet(key);

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

	private void restoreParticipationQueue(Map<String, Double> participationScores, RScoredSortedSet<String> pq) {
		participationScores.forEach((key,value) -> pq.add(value, key));
	}

	private Long extractEventIdFromKey(String key) {
		String[] keyParts = key.split(":");
		return Long.parseLong(keyParts[2]);
	}
}
