package com.bjcareer.stockservice.timeDeal.service;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import org.springframework.data.util.Pair;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class CronQueueService {
	public static final Queue<Pair<Long, String>> queueParticipation = new LinkedList<>();
	private final TimeDealService timeDealService;

	@Scheduled(cron = "0/30 * * * * *")
	public void getParticipationInQueue() {
		log.debug("Cron job started: getParticipationInQueue");

		List<Pair<Long, String>> participations = new ArrayList<>(30);

		for (int i = 0; i < 30; i++) {
			Pair<Long, String> participation = queueParticipation.poll();

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
				log.error(e.getMessage());
				//이미 참여했는지에 따른 처리가 필요함
			}
		});

		log.debug("Cron job finished: getParticipationInQueue");
	}
}
