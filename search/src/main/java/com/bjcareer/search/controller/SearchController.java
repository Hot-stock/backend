package com.bjcareer.search.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.bjcareer.search.controller.dto.QueryRequestDTO;
import com.bjcareer.search.controller.dto.QueryResponseDTO;
import com.bjcareer.search.service.SearchService;


@RestController
@RequiredArgsConstructor
@Slf4j
public class SearchController {
	private final SearchService searchService;

	@PostMapping("/api/v0/search")
	public ResponseEntity<?> search(@RequestBody QueryRequestDTO queryDTO) {
		searchService.updateSearchCount(queryDTO.getKeyword());
		return ResponseEntity.ok(null);
	}

	@GetMapping(("/api/v0/complete/search"))
	public ResponseEntity<?> search(@RequestParam(name = "q", required = false) String query) {
		if(query == null || query.isEmpty()) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		}
		List<String> suggestionKeyword = searchService.getSuggestionKeyword(query);
		QueryResponseDTO queryResponseDTO = new QueryResponseDTO(suggestionKeyword, suggestionKeyword.size());

		return new ResponseEntity<>(queryResponseDTO, HttpStatus.OK);
	}
}