package com.bjcareer.GPTService.event;

import java.util.Optional;

import org.springframework.context.event.EventListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.bjcareer.GPTService.domain.gpt.GPTNewsDomain;
import com.bjcareer.GPTService.domain.gpt.thema.GPTThema;
import com.bjcareer.GPTService.domain.gpt.thema.ThemaInfo;
import com.bjcareer.GPTService.in.dtos.ThemaInfoResponseDTO;
import com.bjcareer.GPTService.out.api.gpt.thema.GPTThemaAdapter;
import com.bjcareer.GPTService.out.persistence.document.GPTThemaNewsRepository;
import com.bjcareer.GPTService.out.persistence.redis.RedisThemaRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RequiredArgsConstructor
public class AnalyzeEventHandler {
	private final GPTThemaAdapter gptThemaAdapter;
	private final KafkaTemplate<String, ThemaInfo> kafkaTemplate;
	private final GPTThemaNewsRepository gptThemaNewsRepository;
	private final RedisThemaRepository redisThemaRepository;

	@EventListener
	@Async
	public void extractThemaInStockNews(GPTNewsDomain news) {
		log.debug("Extract Thema in Stock News: {}", news.getLink());

		if (!news.isRelated()) {
			log.debug("주식과 상관없는 뉴스임으로 분석하지 않아도 됨");
			return;
		}

		String thema = redisThemaRepository.loadThema();

		if (thema.isEmpty()) {
			log.error("Thema is empty Go to fail queue");
			redisThemaRepository.uploadToFailSet(news);
			return;
		}

		Optional<GPTThema> gptThema = gptThemaAdapter.summaryThemaNews(news.getNews(), thema);

		if (gptThema.isPresent()) {
			log.debug("Extracted Thema: {}", gptThema.get());
			ThemaInfo themaInfo = gptThema.get().getThemaInfo();

			if (themaInfo.getName().isEmpty()) {
				log.debug("추출된 테마가 없습니다.");
				return;
			}

			kafkaTemplate.send("thema-topic", themaInfo);
			redisThemaRepository.updateThema(themaInfo.getName());
			gptThemaNewsRepository.save(gptThema.get());
		}
	}
}
