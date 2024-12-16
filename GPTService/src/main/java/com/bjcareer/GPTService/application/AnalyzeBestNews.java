package com.bjcareer.GPTService.application;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.bjcareer.GPTService.domain.gpt.GPTNewsDomain;
import com.bjcareer.GPTService.out.persistence.document.GPTThemaNewsRepository;

@Service
public class AnalyzeBestNews {
	GPTThemaNewsRepository gptThemaNewsRepository;
	public Optional<GPTNewsDomain> getBestNews(List<GPTNewsDomain> news) {
		List<GPTNewsDomain> list = news.stream()
			.filter(GPTNewsDomain::isRelated)
			.filter(n -> !n.getReason().isEmpty())
			.sorted((n1, n2) -> -(n2.getReason().length() - n1.getReason().length())).toList();

		Optional<GPTNewsDomain> bestNew = list.stream().filter(GPTNewsDomain::isThema).findFirst();

		if (bestNew.isEmpty()) {
			bestNew = list.stream().findFirst();
		}

		return bestNew;
	}

}
