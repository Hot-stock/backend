package com.bjcareer.search.application.search;

import java.util.List;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import com.bjcareer.search.candidate.Trie;
import com.bjcareer.search.domain.entity.Thema;
import com.bjcareer.search.event.SearchedKeyword;
import com.bjcareer.search.out.repository.stock.ThemaRepository;
import com.bjcareer.search.application.port.in.SearchUsecase;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SearchService implements SearchUsecase {
	private final ApplicationEventPublisher eventPublisher;
	private final ThemaRepository themaRepository;
	private final Trie trie;

	public List<Thema> getSearchResult(String keyword) {
		List<Thema> resultOfSearch = themaRepository.findAllByKeywordExactlySame(keyword);

		if (!resultOfSearch.isEmpty()) {
			eventPublisher.publishEvent(new SearchedKeyword(keyword));
		}
		return resultOfSearch;
	}

	public List<String> getSuggestionKeyword(String keyword) {
		return trie.search(keyword);
	}
}
