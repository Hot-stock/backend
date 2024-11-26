package com.bjcareer.GPTService.schedule;

import java.util.Optional;

import org.springframework.context.event.EventListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.bjcareer.GPTService.domain.gpt.GPTNewsDomain;
import com.bjcareer.GPTService.domain.gpt.thema.GPTThema;
import com.bjcareer.GPTService.out.api.gpt.thema.GPTThemaAdapter;
import com.bjcareer.GPTService.out.persistence.document.GPTThemaNewsRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RequiredArgsConstructor
public class AnalyzeEventHandler {
	private final GPTThemaAdapter gptThemaAdapter;
	private final KafkaTemplate<String, String> kafkaTemplate;
	private final GPTThemaNewsRepository gptThemaNewsRepository;

	@EventListener
	@Async
	public void extractThemaInStockNews(GPTNewsDomain news) {
		log.debug("Extract Thema in Stock News: {}", news);

		if (!news.isRelated()) {
			log.debug("주식과 상관없는 뉴스임으로 분석하지 않아도 됨");
			return;
		}

		Optional<GPTThema> gptThema = gptThemaAdapter.summaryThemaNews(news.getNews(), "");

		if (gptThema.isPresent()) {
			//kafka 전송 search에게
			// kafkaTemplate.send("thema-topic", gptThema.get().getThemaInfo().getName());
			log.debug("Extracted Thema: {}", gptThema.get().getThemaInfo());
			gptThemaNewsRepository.save(gptThema.get());
		}
	}
}
