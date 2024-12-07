package com.bjcareer.gateway.in.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bjcareer.gateway.application.ports.out.SearchServerPort;
import com.bjcareer.gateway.common.Logger;
import com.bjcareer.gateway.domain.ResponseDomain;
import com.bjcareer.gateway.out.api.search.response.NextEventNewsDTO;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@Slf4j
public class EventController {
	private final SearchServerPort searchServerPort;

	@GetMapping("/api/v0/event")
	@Operation(summary = "앞으로 남은 일정들 조회 가능", description = "앞으로 남은 일정들을 사용자에게 알려줍니다."
	)
	public ResponseEntity<ResponseDomain<NextEventNewsDTO>> searchResult() {
		ResponseDomain<NextEventNewsDTO> result = searchServerPort.getNextEventNews();
		return new ResponseEntity<>(result, result.getStatusCode());
	}

	@GetMapping("/api/v0/event/next-schedule")
	@Operation(summary = "해당 주식이 오를 수 있는 일정과 그동안의 뉴스들을 조회", description = "현재 검색 키워드는 '특징주 + 주식이름'을 통해서 네이버 뉴스를 크롤링하고, 다음 일정 및 뉴스 요약을 위해서 GPT API를 사용함 따라서 많은 시간이 소요됨")
	public ResponseEntity<ResponseDomain<NextEventNewsDTO>> findNextScheduleOfStock(
		@RequestParam(name = "q") String keyword) {
		log.info("Request: {}", keyword);

		ResponseDomain<NextEventNewsDTO> nextScheduleOfStock = searchServerPort.getNextEventNewsFilterByStockName(
			keyword);
		return new ResponseEntity<>(nextScheduleOfStock, nextScheduleOfStock.getStatusCode());
	}

	private boolean validationKeyword(String query) {
		return query == null || query.isEmpty();
	}

}
