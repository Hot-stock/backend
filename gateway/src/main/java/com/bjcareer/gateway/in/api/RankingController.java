package com.bjcareer.gateway.in.api;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bjcareer.gateway.application.ports.out.SearchServerPort;
import com.bjcareer.gateway.domain.ResponseDomain;
import com.bjcareer.gateway.in.api.response.TreeMapResponseDTO;
import com.bjcareer.gateway.out.api.search.response.RankStocksResponseDTO;
import com.bjcareer.gateway.out.api.search.response.TopNewsDTO;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v0/rank")
public class RankingController {
	private final SearchServerPort searchServerPort;

	@GetMapping("/news")
	@Operation(summary = "당일 상승 종목 top 20의 뉴스들을 돌려줌", description = "로그인 요청 기능입니다.", responses = {
		@ApiResponse(responseCode = "200", description = "요청 성공"),
	})
	public ResponseEntity<ResponseDomain<TopNewsDTO>> getStockRank() {
		ResponseDomain<TopNewsDTO> response = searchServerPort.findTopStockNews();
		log.info("Register response: {}", response);
		return new ResponseEntity<>(response, response.getStatusCode());
	}

	// @APIRateLimit
	@GetMapping("/keywords")
	@Operation(
		summary = "키워드 기반 상승률 요청",
		description = "하루 기준으로 주식 검색량의 상승률을 확인하여 top 10의 종목을 돌려줍니다."
	)
	public ResponseEntity<ResponseDomain<RankStocksResponseDTO>> getKeywordCount() {
		ResponseDomain<RankStocksResponseDTO> response = searchServerPort.getRankingStock();
		log.info("Response keyword: {}", response);
		return new ResponseEntity<>(response, response.getStatusCode());
	}

	@GetMapping("/suggestion/stocks")
	@Operation(summary = "추천 주식 조회", description = "추천 주식을 알려줍니다")
	public ResponseEntity<ResponseDomain<RankStocksResponseDTO>> searchTreeMap() {
		log.info("Request tree map");
		ResponseDomain<RankStocksResponseDTO> suggestionStock = searchServerPort.getSuggestionStock();
		return new ResponseEntity<>(suggestionStock, suggestionStock.getStatusCode());
	}
}
