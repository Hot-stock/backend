package com.bjcareer.GPTService.in;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bjcareer.GPTService.application.GPTStockAnalyzeService;
import com.bjcareer.GPTService.domain.gpt.GPTNewsDomain;
import com.bjcareer.GPTService.in.dtos.AnalyzeStockNewsRequestDTO;
import com.bjcareer.GPTService.in.dtos.ListStockNewsResponseDTO;
import com.bjcareer.GPTService.in.dtos.RankingStocksDTO;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v0/analyze")
@RequiredArgsConstructor
public class GPTAnalyzeController {
	private final GPTStockAnalyzeService gptStockNewsService;
	private final KafkaTemplate<String, RankingStocksDTO> kafkaTemplate;

	@PostMapping("/stock-news")
	public ResponseEntity<ListStockNewsResponseDTO> getStockNews(@RequestBody AnalyzeStockNewsRequestDTO request) {
		List<GPTNewsDomain> raiseReasons = gptStockNewsService.analyzeStockNewsByDateWithStockName(request.getDate(), request.getStockName());
		ListStockNewsResponseDTO listStockNewsDTO = new ListStockNewsResponseDTO(request.getStockName(), raiseReasons);
		return ResponseEntity.ok(listStockNewsDTO);
	}
}
