package com.bjcareer.gateway.in.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bjcareer.gateway.application.ports.in.StockInfoCommand;
import com.bjcareer.gateway.application.ports.out.SearchServerPort;
import com.bjcareer.gateway.common.Logger;
import com.bjcareer.gateway.domain.ResponseDomain;
import com.bjcareer.gateway.in.api.request.StockAdditionRequestDTO;
import com.bjcareer.gateway.in.api.response.StockAdditionResponseDTO;
import com.bjcareer.gateway.out.api.search.response.NextScheduleOfStockDTO;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v0/stock")
public class StockController {
	private final SearchServerPort port;
	private final Logger log;

	@PostMapping
	@Operation(summary = "테마 추가 기능", description = "검색되지 않은 테마를 사용자가 추가할 수 있음.")
	public ResponseEntity<ResponseDomain<StockAdditionResponseDTO>> addStockOfThema(@Valid @RequestBody StockAdditionRequestDTO requestDTO) {
		log.info("Request: {}", requestDTO);

		StockInfoCommand stockInfoCommand = new StockInfoCommand(requestDTO.getStockName(), requestDTO.getThema(),
			requestDTO.getCode());

		ResponseDomain<StockAdditionResponseDTO> stockAdditionRequestDTOResponseDomain = port.addStockInfo(
			stockInfoCommand);

		return new ResponseEntity<>(stockAdditionRequestDTOResponseDomain, stockAdditionRequestDTOResponseDomain.getStatusCode());
	}

	@GetMapping("/next-schedule")
	@Operation(summary = "해당 주식이 오를 수 있는 일정과 그동안의 뉴스들을 조회", description = "현재 검색 키워드는 '특징주 + 주식이름'을 통해서 네이버 뉴스를 크롤링하고, 다음 일정 및 뉴스 요약을 위해서 GPT API를 사용함 따라서 많은 시간이 소요됨")
	public ResponseEntity<ResponseDomain<NextScheduleOfStockDTO>> findNextScheduleOfStock(@RequestParam(name = "q") String keyword) {
		log.info("Request: {}", keyword);

		ResponseDomain<NextScheduleOfStockDTO> nextScheduleOfStock = port.findNextScheduleOfStock(keyword);

		return new ResponseEntity<>(nextScheduleOfStock, nextScheduleOfStock.getStatusCode());
	}
}
