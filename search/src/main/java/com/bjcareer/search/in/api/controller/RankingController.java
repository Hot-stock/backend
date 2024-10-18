package com.bjcareer.search.in.api.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bjcareer.search.application.port.in.RankingUsecase;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v0/ranking")
@RequiredArgsConstructor
public class RankingController {
	private final RankingUsecase usecase;

	@GetMapping
	@Operation(summary = "랭킹 조회 기능", description = "사용자의 검색어 랭킹을 조회할 수 있습니다" + "지금은 폴링이지만 나중에는 웹소켓으로 연동 가능.")
	public ResponseEntity<Map<String, List<String>>> getRanking(
		@RequestParam(name = "q", required = false, defaultValue = "10") Integer index) {
		List<String> rankKeyword = usecase.getRankKeyword(index);
		Map<String, List<String>> response = Map.of("rank", rankKeyword);
		return ResponseEntity.ok(response);
	}
}
