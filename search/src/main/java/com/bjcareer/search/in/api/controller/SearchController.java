package com.bjcareer.search.in.api.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bjcareer.search.application.port.in.SearchUsecase;
import com.bjcareer.search.domain.entity.Thema;
import com.bjcareer.search.in.api.controller.dto.SearchResultResponseDTO;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@Slf4j
public class SearchController {
	private final SearchUsecase usecase;

	@GetMapping("/api/v0/thema/sr")
	@Operation(summary = "검색 결과 조회", description = "사용자가 요청한 검색어를 기반으로 검색된 결과를 Return합니다.")
	public ResponseEntity<SearchResultResponseDTO> filterThemesByQuery(@RequestParam(name = "q") String query) {
		if (validationKeyword(query)) {
			return ResponseEntity.badRequest().build();
		}

		List<Thema> searchResult = usecase.filterThemesByQuery(query);
		return ResponseEntity.ok(new SearchResultResponseDTO(query, searchResult));
	}

	private boolean validationKeyword(String query) {
		return query == null || query.isEmpty();
	}
}
