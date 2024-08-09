package com.bjcareer.search.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.bjcareer.search.controller.dto.QueryRequestDTO;
import com.bjcareer.search.controller.dto.QueryResponseDTO;
import com.bjcareer.search.domain.entity.Suggestion;
import com.bjcareer.search.service.SearchService;

@Controller("/api/v0/search")
public class Search {
	SearchService searchService;

	@GetMapping()
	public ResponseEntity<?> search(@RequestParam(name = "q", required = false) String query) {
		List<Suggestion> suggestionKeyword = searchService.getSuggestionKeyword(query);
		QueryResponseDTO queryResponseDTO = new QueryResponseDTO(suggestionKeyword);

		return new ResponseEntity<>(queryResponseDTO, HttpStatus.OK);
	}

	@PostMapping("")
	public String search(@RequestBody QueryRequestDTO queryDTO) {
		Suggestion suggestion = searchService.updateSearchCount(queryDTO.getKeyword());
		return "ok";
	}
}
