package com.bjcareer.GPTService.in;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bjcareer.GPTService.application.GPTStockNewsService;
import com.bjcareer.GPTService.domain.gpt.GPTNewsDomain;
import com.bjcareer.GPTService.in.dtos.AnalyzeStockNewsRequestDTO;
import com.bjcareer.GPTService.in.dtos.ListStockNewsResponseDTO;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v0/analyze")
@RequiredArgsConstructor
public class GPTAnalyzeController {
	private final GPTStockNewsService gptStockNewsService;

	@PostMapping("/stock-news")
	public ResponseEntity<ListStockNewsResponseDTO> getStockNews(@RequestBody AnalyzeStockNewsRequestDTO request) {
		List<GPTNewsDomain> raiseReasons = gptStockNewsService.analyzeStockNews(request.getDate(), request.getStockName());
		ListStockNewsResponseDTO listStockNewsDTO = new ListStockNewsResponseDTO(request.getStockName(), raiseReasons);
		return ResponseEntity.ok(listStockNewsDTO);
	}
}
