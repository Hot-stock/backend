package com.bjcareer.stockservice.timeDeal.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.redisson.api.RScoredSortedSet;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.bjcareer.stockservice.timeDeal.domain.event.exception.InvalidEventException;
import com.bjcareer.stockservice.timeDeal.domain.redis.Redis;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class CronQueueService {

	public static final int INITIAL_CAPACITY = 1000;
	private final Redis redis;
	private final TimeDealService timeDealService;

	@Scheduled(cron = "0/30 * * * * *")
	public void processParticipationQueue() {
		log.debug("Cron job started: processParticipationQueue");

		String key = redis.getSingleKeyUsingScan(TimeDealService.QUEUE_NAME + "*");

		if (key == null) {
			return;
		}

		RScoredSortedSet<String> pq = redis.getScoredSortedSet(key);
		Map<String, Double> participationScores = new HashMap<>(INITIAL_CAPACITY);

		// RScoredSortedSet에서 데이터를 가져와서 Map에 저장
		for (int i = 0; i < INITIAL_CAPACITY; i++) {
			Double score = pq.firstScore();
			String client = pq.pollFirst();
			if (client == null) {
				redis.removeCacheScoredSortedSet(key);
				break; // 더 이상 요소가 없으면 루프를 종료합니다.
			}
			participationScores.put(client, score);
		}

		Long eventId = extractedEventID(key);
		int excessCoupons = 0;
		try {
			excessCoupons = timeDealService.updateEventStatus(eventId, participationScores.size());
		} catch (IllegalStateException e) {
			log.error(e.getMessage());
			restorePQ(participationScores, pq);
		}catch (InvalidEventException e){
			log.error(e.getMessage());
			return;
		}

		List<String> validParticipants =  new ArrayList<>(participationScores.keySet());
		if (excessCoupons > 0) {
			int validParticipantsCount = participationScores.size() - excessCoupons;
			validParticipants = validParticipants.subList(0, validParticipantsCount);
		}

		timeDealService.bulkGenerateCoupon(eventId, validParticipants);
	}

	private void restorePQ(Map<String, Double> participationScores, RScoredSortedSet<String> pq) {
		participationScores.keySet().forEach(client -> pq.add(participationScores.get(client), client));
	}

	private Long extractedEventID(String keysByPattern) {
		String[] split = keysByPattern.split(":");
		return Long.parseLong(split[2]);
	}
}
