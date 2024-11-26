package com.bjcareer.GPTService.schedule;

import java.util.List;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.bjcareer.GPTService.domain.gpt.GPTNewsDomain;
import com.bjcareer.GPTService.out.persistence.redis.RedisThemaRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class RecoverEvent {
	private final ApplicationEventPublisher applicationEventPublisher;
	private final RedisThemaRepository redisThemaRepository;

	@Scheduled(cron = "0 */5 * * * *")
	void recoverExtractThemaInNews() {
		String thema = redisThemaRepository.loadThema();

		if (thema.isEmpty()) {
			log.error("thema가 아직 복구되지 않았습니다.");
			return;
		}

		List<GPTNewsDomain> newsInFailSet = redisThemaRepository.getNewsInFailSet();
		applicationEventPublisher.publishEvent(newsInFailSet);
	}

}
