package com.bjcareer.search.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bjcareer.search.controller.dto.QueryResponseDTO;
import com.bjcareer.search.controller.dto.SearchResultResponseDTO;
import com.bjcareer.search.domain.entity.Thema;
import com.bjcareer.search.service.SearchService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@Slf4j
public class SearchController {
	private final SearchService searchService;

	//추천 검색어 기능을 위한 것
	@GetMapping("/api/v0/search")
	public ResponseEntity search(@RequestParam(name = "q") String query) {
		if (validationKeyword(query)) {
			return ResponseEntity.badRequest().build();
		}

		List<String> suggestionKeyword = searchService.getSuggestionKeyword(query);
		QueryResponseDTO queryResponseDTO = new QueryResponseDTO(suggestionKeyword, suggestionKeyword.size());

		return new ResponseEntity<>(queryResponseDTO, HttpStatus.OK);
	}

	//검색 결과를 요청하는 곳임
	@GetMapping("/api/v0/sr")
	public ResponseEntity searchResult(@RequestParam(name = "q") String query) {
		if (validationKeyword(query)) {
			return ResponseEntity.badRequest().build();
		}

		List<Thema> searchResult = searchService.getSearchResult(query);
		return ResponseEntity.ok(new SearchResultResponseDTO(query, searchResult));
	}

	private boolean validationKeyword(String query) {
		return query == null || query.isEmpty();
	}
}
