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

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@Slf4j
public class SearchController {
	private final SearchService searchService;


	@GetMapping("/api/v0/search")
	@Operation(summary = "검색어 후보 기능", description = "사용자가 검색을 할 때, 검색어를 입력하면 검색어 후보를 Return합니다.")
	public ResponseEntity<QueryResponseDTO> search(@RequestParam(name = "q") String query) {
		if (validationKeyword(query)) {
			return ResponseEntity.badRequest().build();
		}

		List<String> suggestionKeyword = searchService.getSuggestionKeyword(query);
		QueryResponseDTO queryResponseDTO = new QueryResponseDTO(suggestionKeyword, suggestionKeyword.size());

		return new ResponseEntity<>(queryResponseDTO, HttpStatus.OK);
	}

	@GetMapping("/api/v0/sr")
	@Operation(summary = "검색 결과 조회", description = "사용자가 요청한 검색어를 기반으로 검색된 결과를 Return합니다.")
	public ResponseEntity<SearchResultResponseDTO> searchResult(@RequestParam(name = "q") String query) {
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
