package com.bjcareer.GPTService.event;

import java.util.List;
import java.util.Optional;

import org.springframework.context.event.EventListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.bjcareer.GPTService.domain.gpt.GPTNewsDomain;
import com.bjcareer.GPTService.domain.gpt.thema.GPTStockThema;
import com.bjcareer.GPTService.domain.gpt.thema.ThemaInfo;
import com.bjcareer.GPTService.out.api.gpt.thema.stockNews.GPTThemaOfStockNewsAdapter;
import com.bjcareer.GPTService.out.persistence.document.GPTThemaNewsRepository;
import com.bjcareer.GPTService.out.persistence.redis.RedisThemaRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RequiredArgsConstructor
public class AnalyzeEventHandler {
	private final GPTThemaOfStockNewsAdapter gptThemaOfStockNewsAdapter;
	private final KafkaTemplate<String, byte[]> kafkaTemplate;
	private final GPTThemaNewsRepository gptThemaNewsRepository;
	private final RedisThemaRepository redisThemaRepository;
	private final ObjectMapper objectMapper;

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

		Optional<GPTStockThema> byLink = gptThemaNewsRepository.findByLink(news.getLink());

		if (byLink.isPresent()) {
			GPTStockThema gptStockThema = byLink.get();
			log.debug("Already Extracted Thema: {}", gptStockThema.getThemaInfo());
			return;
		}

		Optional<GPTStockThema> gptThema = gptThemaOfStockNewsAdapter.getThema(news, thema);

		if (gptThema.isPresent()) {
			log.debug("Extracted Thema: {}", gptThema.get());
			GPTStockThema gptStockThema = gptThema.get();

			if (!gptStockThema.isRelated()) {
				return;
			}

			gptThemaNewsRepository.save(gptStockThema);
			List<ThemaInfo> themaInfo = gptThema.get().getThemaInfo();
			themaInfo.forEach(this::sendThemaToKafka);
		}
	}

	private void sendThemaToKafka(ThemaInfo themaInfo) {
		log.debug("Send Thema to Kafka: {}", themaInfo);
		if (!isNeedToSendToKafka(themaInfo)) {
			return;
		}

		try {
			byte[] bytes = objectMapper.writeValueAsBytes(themaInfo);
			kafkaTemplate.send("thema-topic", bytes);
		} catch (JsonProcessingException e) {
			throw new RuntimeException(e);
		}

		redisThemaRepository.updateThema(themaInfo.getName());
	}

	private boolean isNeedToSendToKafka(ThemaInfo themaInfo) {
		return !themaInfo.getName().isEmpty() && !themaInfo.getStockName().isEmpty();
	}
}
