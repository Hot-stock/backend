package com.bjcareer.gateway.in.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bjcareer.gateway.application.ports.in.StockInfoCommand;
import com.bjcareer.gateway.application.ports.out.SearchServerPort;
import com.bjcareer.gateway.domain.ResponseDomain;
import com.bjcareer.gateway.in.api.request.StockAdditionRequestDTO;
import com.bjcareer.gateway.in.api.response.StockAdditionResponseDTO;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v0/stock")
@Slf4j
public class StockController {
	private final SearchServerPort port;

	@PostMapping
	@Operation(summary = "테마 추가 기능", description = "검색되지 않은 테마를 사용자가 추가할 수 있음.")
	public ResponseEntity<ResponseDomain<StockAdditionResponseDTO>> addStockOfThema(@Valid @RequestBody StockAdditionRequestDTO requestDTO) {
		log.debug("request: {}", requestDTO);

		StockInfoCommand stockInfoCommand = new StockInfoCommand(requestDTO.getStockName(), requestDTO.getThema(),
			requestDTO.getCode());

		ResponseDomain<StockAdditionResponseDTO> stockAdditionRequestDTOResponseDomain = port.addStockInfo(
			stockInfoCommand);

		return new ResponseEntity<>(stockAdditionRequestDTOResponseDomain, stockAdditionRequestDTOResponseDomain.getStatusCode());
	}
}
