package com.bjcareer.gateway.out.api.search;

import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;

import com.bjcareer.gateway.application.ports.out.KeywordCommand;
import com.bjcareer.gateway.common.Logger;
import com.bjcareer.gateway.domain.ResponseDomain;
import com.bjcareer.gateway.domain.SearchCandidate;
import com.bjcareer.gateway.domain.SearchResult;
import com.bjcareer.gateway.out.api.CommonConfig;
import com.bjcareer.gateway.out.api.search.response.TopNewsDTO;

class SearchServerAPIAdapterTest {
	Logger logger = new Logger();

	@Test
	void search() {
		// Given
		WebClient webClient = CommonConfig.createWebClient("http://43.201.52.244:8080");
		SearchServerAPIAdapter searchServerAPIAdapter = new SearchServerAPIAdapter(webClient, logger);
		SearchCandidate r = searchServerAPIAdapter.searchCandidate(new KeywordCommand("덕"));

		System.out.println("r = " + r.getKeywords().get(0).getKeyword());
	}

	@Test
	void searchResult() {
		// Given

		WebClient webClient = CommonConfig.createWebClient("http://43.201.52.244:8080");
		SearchServerAPIAdapter searchServerAPIAdapter = new SearchServerAPIAdapter(webClient, logger);
		SearchResult r = searchServerAPIAdapter.searchResult(new KeywordCommand("덕우전자"));

		System.out.println("r = " + r);
	}

	@Test
	void topNews요청() {
		// Given
		WebClient webClient = CommonConfig.createWebClient("http://localhost:8080");
		SearchServerAPIAdapter searchServerAPIAdapter = new SearchServerAPIAdapter(webClient, logger);
		ResponseDomain<TopNewsDTO> topStockNews = searchServerAPIAdapter.findTopStockNews();

		System.out.println("topStockNews = " + topStockNews);
	}
}
