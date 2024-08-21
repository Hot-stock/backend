package com.bjcareer.stockservice.timeDeal.service;

import java.util.ArrayList;
import java.util.List;

import org.redisson.api.RScoredSortedSet;
import org.springframework.data.util.Pair;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class CronQueueService {
	public final RScoredSortedSet<Pair<Long, String>> priorityQueue;
	private final TimeDealService timeDealService;

	@Scheduled(cron = "0/30 * * * * *")
	public void getParticipationInQueue() {
		log.debug("Cron job started: getParticipationInQueue");

		List<Pair<Long, String>> participations = new ArrayList<>(30);

		for (int i = 0; i < 30; i++) {
			Pair<Long, String> participation = priorityQueue.pollFirst();

			if (participation == null) {
				log.debug("Queue is empty, breaking out of the loop");
				break;
			}

			participations.add(participation);
		}

		log.debug("Processing {} participations", participations.size());

		participations.forEach(participation -> {
			log.info("Generating coupon for user: {}", participation.getSecond());
			try {
				timeDealService.generateCouponToUser(participation.getFirst(), participation.getSecond());
			}catch (Exception e){
				log.debug(e.getMessage());
			}
		});

		log.debug("Cron job finished: getParticipationInQueue");
	}
}
