package com.bjcareer.search.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bjcareer.search.controller.dto.QueryRequestDTO;
import com.bjcareer.search.controller.dto.QueryResponseDTO;
import com.bjcareer.search.domain.entity.Suggestion;
import com.bjcareer.search.service.SearchService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@Slf4j
public class Search {
	private final SearchService searchService;

	@GetMapping(("/api/v0/complete/search"))
	public ResponseEntity<?> search(@RequestParam(name = "q", required = false) String query) {
		if(query == null || query.isEmpty()) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		}

		List<Suggestion> suggestionKeyword = searchService.getSuggestionKeyword(query);
		log.debug("suggestionKeyword: {}", suggestionKeyword);

		QueryResponseDTO queryResponseDTO = new QueryResponseDTO(suggestionKeyword);
		return new ResponseEntity<>(queryResponseDTO, HttpStatus.OK);
	}

	@PostMapping("/api/v0/search")
	public String search(@RequestBody QueryRequestDTO queryDTO) {
		Suggestion suggestion = searchService.updateSearchCount(queryDTO.getKeyword());
		return "ok";
	}
}
