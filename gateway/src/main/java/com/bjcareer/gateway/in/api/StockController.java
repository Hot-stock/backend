package com.bjcareer.gateway.in.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
import com.bjcareer.gateway.in.api.response.CandleResponseDTO;
import com.bjcareer.gateway.in.api.response.StockAdditionResponseDTO;
import com.bjcareer.gateway.out.api.search.response.NextEventNewsDTO;

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

	@GetMapping("/{code}/ohlc")
	@Operation(summary = "차트 OHLC 데이터 요청", description = "일봉데이터 요청")
	public ResponseEntity<ResponseDomain<CandleResponseDTO>> getOHLC(@PathVariable("code") String code, @RequestParam(name = "period", required = false, defaultValue = "day") String period) {
		ResponseDomain<CandleResponseDTO> ohlc = port.getOHLC(code, period);
		return new ResponseEntity<>(ohlc, ohlc.getStatusCode());
	}
}
