package com.bjcareer.search.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bjcareer.search.controller.dto.SearchStockResponseDTO;
import com.bjcareer.search.domain.entity.Thema;
import com.bjcareer.search.service.StockService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v0/stock")
@Slf4j
public class StockController {
	private final StockService stockService;

	@GetMapping("")
	public Response<?> getStockOfThema(@RequestParam String q) {
		log.info("q: {}", q);
		List<Thema> stockOfThema = stockService.getStockOfThema(q);
		SearchStockResponseDTO searchStockResponseDTO = new SearchStockResponseDTO(stockOfThema);
		return new Response<>(HttpStatus.OK, "OK", searchStockResponseDTO);
	}
}
