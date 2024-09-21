package com.bjcareer.search.service;

import java.util.List;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import com.bjcareer.search.domain.entity.Thema;
import com.bjcareer.search.repository.noSQL.DocumentRepository;
import com.bjcareer.search.repository.stock.ThemaRepository;
import com.bjcareer.search.retrieval.Trie;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SearchService {
	private final ApplicationEventPublisher eventPublisher;
	private final ThemaRepository themaRepository;
	private final DocumentRepository documentRepository;
	private final Trie trie;

	public List<Thema> getSearchResult(String keyword) {
		List<Thema> resultOfSearch = themaRepository.findAllByKeywordContaining(keyword);

		if (!resultOfSearch.isEmpty()) {
			eventPublisher.publishEvent(keyword);
		}
		return resultOfSearch;
	}

	public List<String> getSuggestionKeyword(String keyword) {
		return trie.search(keyword);
	}

	public void updateSearchCount(String keyword) {
		documentRepository.updateSearchCount(keyword);
	}

}
