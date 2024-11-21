package com.bjcareer.gateway.in.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bjcareer.gateway.application.ports.out.SearchServerPort;
import com.bjcareer.gateway.common.Logger;
import com.bjcareer.gateway.domain.ResponseDomain;
import com.bjcareer.gateway.out.api.search.response.TopNewsDTO;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v0/news")
public class RankingController {
	private final SearchServerPort searchServerPort;
	private final Logger log;

	@GetMapping("/top-stock")
	@Operation(summary = "당일 상승 종목 top 20의 뉴스들을 돌려줌", description = "로그인 요청 기능입니다.", responses = {
		@ApiResponse(responseCode = "200", description = "요청 성공"),
	})
	public ResponseEntity<ResponseDomain<TopNewsDTO>> getStockRank() {
		ResponseDomain<TopNewsDTO> response = searchServerPort.findTopStockNews();
		log.info("Register response: {}", response);
		return new ResponseEntity<>(response, response.getStatusCode());
	}
}
