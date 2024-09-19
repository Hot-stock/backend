package com.bjcareer.search.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bjcareer.search.service.RankingService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v0/ranking")
@RequiredArgsConstructor
public class RankingController {
	private final RankingService rankingService;

	@GetMapping
	public ResponseEntity<?> getRanking(
		@RequestParam(name = "q", required = false, defaultValue = "10") Integer index) {
		List<String> rankKeyword = rankingService.getRankKeyword(index);
		Map<String, List<String>> response = Map.of("rank", rankKeyword);
		return ResponseEntity.ok(response);
	}
}
