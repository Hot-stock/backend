package com.bjcareer.GPTService.application;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.bjcareer.GPTService.domain.gpt.GPTNewsDomain;
import com.bjcareer.GPTService.domain.gpt.GPTTriggerBackground;
import com.bjcareer.GPTService.domain.gpt.thema.GPTStockThema;
import com.bjcareer.GPTService.out.api.gpt.insight.trigger.ChatGPTTriggerAdapter;
import com.bjcareer.GPTService.out.persistence.document.GPTBackgroundRepository;
import com.bjcareer.GPTService.out.persistence.document.GPTStockNewsRepository;
import com.bjcareer.GPTService.out.persistence.document.GPTStockThemaNewsRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class AnalyzeRaiseBackground {
	private final GPTStockNewsRepository gptStockNewsRepository;
	private final ChatGPTTriggerAdapter chatGPTTriggerAdapter;
	private final GPTStockThemaNewsRepository gptStockThemaNewsRepository;
	private final GPTBackgroundRepository gptBackgroundRepository;

	public GPTTriggerBackground getTriggerReason(String themaName) {
		Optional<GPTTriggerBackground> byThema = gptBackgroundRepository.findByThema(themaName);

		if (byThema.isEmpty()) {
			log.debug("Not found byThema: {}", themaName);
			throw new NoSuchElementException("Not found byThema: " + themaName);
		}

		return byThema.get();
	}

	public Optional<GPTTriggerBackground> loadTriggerOfRiseOfThema(String themaName) {
		List<String> strings = loadAllReasonOfRiseOfThema(themaName);
		Optional<GPTTriggerBackground> trigger = chatGPTTriggerAdapter.getTrigger(strings, themaName,
			ChatGPTTriggerAdapter.GPT_4o);
		trigger.ifPresent(gptBackgroundRepository::save);

		return trigger;
	}

	private List<String> loadAllReasonOfRiseOfThema(String themaName) {
		List<GPTStockThema> byThemaName = gptStockThemaNewsRepository.findByThemaName(themaName);
		log.debug("Load size byThemaName: {}", byThemaName.size());

		List<GPTNewsDomain> domains = byThemaName.stream().map(t -> gptStockNewsRepository.findByLink(t.getLink()))
			.filter(Optional::isPresent)
			.map(Optional::get)
			.toList();

		return domains.stream().map(GPTNewsDomain::getReason).toList();
	}
}
