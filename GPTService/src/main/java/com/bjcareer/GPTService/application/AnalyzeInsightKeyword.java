package com.bjcareer.GPTService.application;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.bjcareer.GPTService.domain.gpt.GPTNewsDomain;
import com.bjcareer.GPTService.domain.gpt.GPTTriggerBackground;
import com.bjcareer.GPTService.domain.gpt.insight.GPTInsight;
import com.bjcareer.GPTService.domain.gpt.thema.GPTStockThema;
import com.bjcareer.GPTService.domain.gpt.thema.GPTThema;
import com.bjcareer.GPTService.out.api.gpt.insight.score.GPTInsightAdapter;
import com.bjcareer.GPTService.out.persistence.document.GPTBackgroundRepository;
import com.bjcareer.GPTService.out.persistence.document.GPTStockNewsRepository;
import com.bjcareer.GPTService.out.persistence.document.GPTStockThemaNewsRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class AnalyzeInsightKeyword {
	private final GPTBackgroundRepository gptBackgroundRepository;
	private final GPTStockThemaNewsRepository gptStockThemaNewsRepository;
	private final GPTStockNewsRepository gptStockNewsRepository;
	private final GPTThemaNewsAnalyzeService gptThemaNewsAnalyzeService;
	private final GPTInsightAdapter insightAdapter;

	public GPTInsight analyzeInsightKeyword(String stockName, LocalDate startDate, LocalDate endDate) {
		log.debug("Tracking reason {}", stockName);
		Set<String> keywords = new HashSet<>();
		List<GPTThema> gptThemas = new ArrayList<>();

		List<GPTNewsDomain> raiseReason = gptStockNewsRepository.findByStockName(stockName);
		List<String> links = raiseReason.stream().map(GPTNewsDomain::getLink).toList();
		List<String> themaName = getThemaName(links);

		List<GPTTriggerBackground> backgrounds = themaName.stream()
			.map(gptBackgroundRepository::findByThema)
			.filter(Optional::isPresent)
			.map(Optional::get)
			.toList();

		log.debug("찾아진 테마들의 background = {}", backgrounds);
		backgrounds.forEach(b -> keywords.addAll(b.getKeywords()));

		for (String keyword : keywords) {
			List<GPTThema> analyzeThemaNews = gptThemaNewsAnalyzeService.getAnalyzeThemaNews(keyword, startDate,
				endDate);

			gptThemas.addAll(analyzeThemaNews);
		}

		return insightAdapter.getInsight(stockName, raiseReason, backgrounds, gptThemas);
	}

	private List<String> getThemaName(List<String> links) {
		List<GPTStockThema> byLinkIn = gptStockThemaNewsRepository.findByLinkIn(links);
		Set<String> collect = byLinkIn.stream().map(t -> t.getThemaInfo().getName()).collect(Collectors.toSet());
		return new ArrayList<>(collect);
	}

}
