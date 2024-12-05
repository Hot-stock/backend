package com.bjcareer.GPTService.application;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.bjcareer.GPTService.domain.gpt.GPTNewsDomain;

@Service
public class AnalyzeBestNews {
	public Optional<GPTNewsDomain> getBestNews(List<GPTNewsDomain> news) {
		Optional<GPTNewsDomain> bestNew = news.stream()
			.filter(GPTNewsDomain::isRelated)
			.filter(n -> !n.getReason().isEmpty())
			.sorted((n1, n2) -> -(n2.getReason().length() - n1.getReason().length()))
			.findFirst();

		return bestNew;
	}

}
