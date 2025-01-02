package com.bjcareer.GPTService.application;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.bjcareer.GPTService.domain.gpt.GPTNewsDomain;
import com.bjcareer.GPTService.domain.gpt.GPTTriggerBackground;
import com.bjcareer.GPTService.out.api.gpt.insight.trigger.GPTTriggerAdapter;
import com.bjcareer.GPTService.out.persistence.document.GPTBackgroundRepository;
import com.bjcareer.GPTService.out.persistence.document.GPTStockNewsRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class AnalyzeRaiseBackground {
	private final GPTStockNewsRepository gptStockNewsRepository;
	private final GPTTriggerAdapter gptTriggerAdapter;
	private final GPTBackgroundRepository gptBackgroundRepository;

	public Optional<GPTTriggerBackground> saveTriggerOfRiseOfThema(String themaName, String stockName) {
		List<GPTNewsDomain> gptNewsDomains = loadAllReasonOfRiseOfThema(themaName);

		if (gptNewsDomains.size() < 3) {
			log.debug("Not enough news for Thema: {}", themaName);
			return Optional.empty();
		}

		if (!isNeedToUpdateBackground(themaName, stockName)) {
			log.debug("Didn't Need to update background for Thema: {}", themaName);
			return Optional.empty();
		}

		// GPTTriggerBackground triggerReason = getTriggerReason(themaName);

		List<String> reason = gptNewsDomains.stream().map(GPTNewsDomain::getReason).collect(Collectors.toList());
		List<String> stockNames = gptNewsDomains.stream().map(GPTNewsDomain::getStockName).toList();

		Optional<GPTTriggerBackground> trigger = gptTriggerAdapter.getTrigger(reason, themaName,
			GPTTriggerAdapter.GPT_4o);

		if (trigger.isPresent()) {
			// triggerReason.addKeywords(trigger.get().getKeywords().stream().toList());
			// triggerReason.addStocks(stockNames);
			trigger.get().addKeywords(trigger.get().getKeywords().stream().toList());
			trigger.get().addStocks(stockNames);
			gptBackgroundRepository.save(trigger.get());
			return Optional.empty();
		}

		return trigger;
	}

	public GPTTriggerBackground getTriggerReason(String themaName) {
		Optional<GPTTriggerBackground> byThema = gptBackgroundRepository.findByThema(themaName);

		if (byThema.isEmpty()) {
			log.debug("Not found byThema: {}", themaName);
			throw new NoSuchElementException("Not found byThema: " + themaName);
		}

		return byThema.get();
	}

	private boolean isNeedToUpdateBackground(String themaName, String stockName) {
		Optional<GPTTriggerBackground> byThema = gptBackgroundRepository.findByThema(themaName);

		if (byThema.isPresent()) {
			log.debug("Found Thema: {}", themaName);
			GPTTriggerBackground gptTriggerBackground = byThema.get();
			return gptTriggerBackground.isNeedToUpdate(stockName);
		}

		return true;
	}

	private List<GPTNewsDomain> loadAllReasonOfRiseOfThema(String themaName) {
		List<GPTNewsDomain> raiseReasonByThemaName = gptStockNewsRepository.findRaiseReasonByThemaName(themaName);
		log.debug("Load all reason of rise of Thema size: {}", raiseReasonByThemaName.size());
		return raiseReasonByThemaName;
	}
}
