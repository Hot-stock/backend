package com.bjcareer.search.in.api.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bjcareer.search.application.port.in.NewsServiceUsecase;
import com.bjcareer.search.application.stock.StockService;
import com.bjcareer.search.domain.entity.Thema;
import com.bjcareer.search.domain.gpt.GPTNewsDomain;
import com.bjcareer.search.in.api.controller.dto.QueryToFindRaiseReasonResponseDTO;
import com.bjcareer.search.in.api.controller.dto.StockAdditionRequestDTO;
import com.bjcareer.search.in.api.controller.dto.StockAdditionResponseDTO;
import com.bjcareer.search.out.api.toss.TossServerAdapter;
import com.bjcareer.search.out.api.toss.dtos.CandleResponseDTO;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v0/stock")
@Slf4j
public class StockController {
	private final StockService stockService;
	private final NewsServiceUsecase newsServiceUsecase;
	private final TossServerAdapter tossServerAdapter;

	@GetMapping("/update")
	@Operation(summary = "주식 정보 갱신", description = "주식 정보를 갱신함")
	public ResponseEntity<Void> updateAllStock() {
		stockService.updateAllStock();
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@GetMapping("/{code}/ohlc")
	@Operation(summary = "차트 데이터 갱신 요청", description = "ohlc 데이터를 갱신함")
	public ResponseEntity<CandleResponseDTO> updateAllStock(@PathVariable("code") String code,
		@RequestParam(name = "period", required = false, defaultValue = "day") String period) {
		CandleResponseDTO stockPriceURI = tossServerAdapter.getStockPriceURI(code, period);
		return new ResponseEntity<>(stockPriceURI, HttpStatus.OK);
	}


	@PostMapping
	@Operation(summary = "테마 추가 기능", description = "검색되지 않은 테마를 사용자가 추가할 수 있음.")
	public ResponseEntity<StockAdditionResponseDTO>addStockOfThema(@Valid @RequestBody StockAdditionRequestDTO requestDTO) {
		log.debug("request: {}", requestDTO);

		Thema thema = stockService.addStockThema(requestDTO.getCode(), requestDTO.getStockName(),
			requestDTO.getThema());

		StockAdditionResponseDTO responseDTO = new StockAdditionResponseDTO(thema.getId(),
			thema.getStock().getName(), thema.getThemaInfo().getName(), thema.getStock().getCode());

		return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
	}

	@GetMapping("/reason")
	@Operation(summary = "주식이 해당 일자의 뉴스의 상승 이유를 찾아서 보여줌", description = "name: 주식 이름, date: 날짜(yyyy-MM-dd)")
	public ResponseEntity<QueryToFindRaiseReasonResponseDTO> searchStockRaiseReason(
		@RequestParam(name = "name") String stockName, @RequestParam(name = "date") LocalDate date) {
		log.debug("request: {} {} ", stockName, date);

		List<GPTNewsDomain> raiseReasonThatDate = newsServiceUsecase.findRaiseReasonThatDate(stockName, date);

		if (raiseReasonThatDate.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

		QueryToFindRaiseReasonResponseDTO responseDTO = new QueryToFindRaiseReasonResponseDTO(raiseReasonThatDate);

		return new ResponseEntity<>(responseDTO, HttpStatus.OK);
	}
}
