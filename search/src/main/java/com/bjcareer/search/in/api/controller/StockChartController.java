package com.bjcareer.search.in.api.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bjcareer.search.application.port.in.StockManagementUsecase;
import com.bjcareer.search.in.api.controller.dto.UpdateStockChartRequestDTO;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v0/chart")
public class StockChartController {
	private final StockManagementUsecase usecase;

	@PostMapping("/renew")
	public ResponseEntity<HttpStatus> updateStockChart(@RequestBody UpdateStockChartRequestDTO requestDTO) {
		usecase.addStockChart(requestDTO.getStockName());
		return ResponseEntity.ok().build();
	}
}
