package com.bjcareer.search.application.search;

import java.util.List;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import com.bjcareer.search.application.port.in.SearchUsecase;
import com.bjcareer.search.application.port.out.persistence.thema.LoadThemaUsingkeywordCommand;
import com.bjcareer.search.application.port.out.persistence.thema.ThemaRepositoryPort;
import com.bjcareer.search.candidate.Trie;
import com.bjcareer.search.domain.entity.Thema;
import com.bjcareer.search.event.SearchedKeyword;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SearchService implements SearchUsecase {
	private final ApplicationEventPublisher eventPublisher;
	private final ThemaRepositoryPort themaRepositoryPort;
	private final Trie trie;

	public List<Thema> filterThemesByQuery(String keyword) {
		LoadThemaUsingkeywordCommand command = new LoadThemaUsingkeywordCommand(keyword);
		List<Thema> resultOfSearch = themaRepositoryPort.loadAllByKeywordContaining(command);

		if (!resultOfSearch.isEmpty()) {
			eventPublisher.publishEvent(new SearchedKeyword(keyword));
		}
		return resultOfSearch;
	}

	public List<String> getSuggestionKeyword(String keyword) {
		return trie.search(keyword);
	}
}
