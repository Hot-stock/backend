package com.bjcareer.gateway.out.api.search;

import java.util.List;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import com.bjcareer.gateway.application.ports.out.KeywordCommand;
import com.bjcareer.gateway.application.ports.out.KeywordServerPort;
import com.bjcareer.gateway.application.ports.out.SearchServerPort;
import com.bjcareer.gateway.domain.AbsoluteRankKeyword;
import com.bjcareer.gateway.domain.SearchCandidate;
import com.bjcareer.gateway.domain.SearchResult;

@Component
public class SearchServerAPIAdapter implements KeywordServerPort, SearchServerPort {
	private final WebClient webClient;

	public SearchServerAPIAdapter(@Qualifier("searchWebClient") WebClient webClient) {
		this.webClient = webClient;
	}

	@Override
	public List<AbsoluteRankKeyword> searchCount(KeywordCommand command) {
		List<AbsoluteRankKeyword> result = webClient.get()
			.uri(SearchServerURI.KEYWORD_COUNT + "?q=" + command.getKeyword())
			.retrieve()
			.bodyToMono(new ParameterizedTypeReference<List<AbsoluteRankKeyword>>() {
			})
			.block();  // 동기적으로 결과 대기

		return result;  // 결과 반환
	}

	@Override
	public SearchCandidate searchCandidate(KeywordCommand command) {
		SearchCandidate result = webClient.get()
			.uri(SearchServerURI.SEARCH_CANDIDATE + "?q=" + command.getKeyword())
			.retrieve()
			.bodyToMono(SearchCandidate.class)
			.block();  // 동기적으로 결과 대기

		return result;  // 결과 반환
	}

	@Override
	public SearchResult searchResult(KeywordCommand command) {
		SearchResult result = webClient.get()
			.uri(SearchServerURI.SEARCH_RESULT + "?q=" + command.getKeyword())
			.retrieve()
			.bodyToMono(SearchResult.class)
			.block();  // 동기적으로 결과 대기

		return result;
	}
}
