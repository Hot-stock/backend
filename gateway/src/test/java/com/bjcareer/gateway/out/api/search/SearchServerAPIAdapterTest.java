package com.bjcareer.gateway.out.api.search;

import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;

import com.bjcareer.gateway.application.ports.out.KeywordCommand;
import com.bjcareer.gateway.domain.SearchCandidate;
import com.bjcareer.gateway.domain.SearchResult;
import com.bjcareer.gateway.out.api.CommonConfig;

class SearchServerAPIAdapterTest {
	@Test
	void search() {
		// Given
		WebClient webClient = CommonConfig.createWebClient("http://43.201.52.244:8080");
		SearchServerAPIAdapter searchServerAPIAdapter = new SearchServerAPIAdapter(webClient);
		SearchCandidate r = searchServerAPIAdapter.searchCandidate(new KeywordCommand("덕"));

		System.out.println("r = " + r.getKeywords().get(0).getKeyword());
	}

	@Test
	void searchResult() {
		// Given
		WebClient webClient = CommonConfig.createWebClient("http://43.201.52.244:8080");
		SearchServerAPIAdapter searchServerAPIAdapter = new SearchServerAPIAdapter(webClient);
		SearchResult r = searchServerAPIAdapter.searchResult(new KeywordCommand("덕우전자"));

		System.out.println("r = " + r);
	}
}
