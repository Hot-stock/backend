package com.bjcareer.search.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.bjcareer.search.domain.entity.Suggestion;
import com.bjcareer.search.repository.SearchRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SearchService {
	private final SearchRepository searchRepository;

	public List<Suggestion> getSuggestionKeyword(String keyword){
		return searchRepository.findSuggestionKeyword(keyword);
	}

	public Suggestion updateSearchCount(String keyword){
		Suggestion suggestion = searchRepository.updateSearchCount(keyword);
		return suggestion;
	}

}
