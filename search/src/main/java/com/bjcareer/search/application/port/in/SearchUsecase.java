package com.bjcareer.search.application.port.in;

import java.util.List;

import com.bjcareer.search.domain.entity.Thema;

public interface SearchUsecase {
	List<Thema> getSearchResult(String keyword, int page, int size);

	List<String> getSuggestionKeyword(String keyword);
}
