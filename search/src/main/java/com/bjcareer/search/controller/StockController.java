package com.bjcareer.search.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bjcareer.search.controller.dto.SearchStockResponseDTO;
import com.bjcareer.search.controller.dto.StockAdditionRequestDTO;
import com.bjcareer.search.controller.dto.StockAdditionResponseDTO;
import com.bjcareer.search.domain.entity.Thema;
import com.bjcareer.search.service.RankingService;
import com.bjcareer.search.service.StockService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v0/stock")
@Slf4j
public class StockController {
	private final StockService stockService;
	private final RankingService rankingService;

	@GetMapping("")
	public ResponseEntity<> getStockOfThema(@RequestParam(name = "q") String stockKeyword) {
		log.debug("q: {}", stockKeyword);
		List<Thema> stockOfThema = stockService.getStockOfThema(stockKeyword);

		if (!stockOfThema.isEmpty()) {
			rankingService.updateKeyword(stockKeyword);
		}
		SearchStockResponseDTO searchStockResponseDTO = new SearchStockResponseDTO(stockOfThema);
		return ResponseEntity.status(HttpStatus.OK).body(searchStockResponseDTO.responses);
	}

	@PostMapping
	public ResponseEntity<?> addStockOfThema(@Valid @RequestBody StockAdditionRequestDTO requestDTO) {
		log.debug("request: {}", requestDTO);

		Thema thema = stockService.addStockThema(requestDTO.getCode(), requestDTO.getStockName(),
			requestDTO.getThema());

		StockAdditionResponseDTO responseDTO = new StockAdditionResponseDTO(thema.getId(),
			thema.getStock().getName(), thema.getThemaInfo().getName(), thema.getStock().getCode());
		return ResponseEntity.status(HttpStatus.CREATED)
			.body(new Response<>(HttpStatus.CREATED, "CREATED", responseDTO));
	}
}
