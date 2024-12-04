package com.bjcareer.search.application.information;

import java.util.List;

import org.springframework.stereotype.Service;

import com.bjcareer.search.domain.gpt.GPTNewsDomain;
import com.bjcareer.search.out.persistence.noSQL.DocumentAnalyzeRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NextEventService {
	private final DocumentAnalyzeRepository documentAnalyzeRepository;

	public List<GPTNewsDomain> getUpcomingEvents(){
		return documentAnalyzeRepository.getUpcomingNews();
	}

	public List<GPTNewsDomain> filterUpcomingEventsByStockName(String stockName) {
		return documentAnalyzeRepository.getUpcomingNewsByStockName(stockName);
	}
}
