package com.bjcareer.search.in.api.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.util.Pair;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bjcareer.search.application.port.in.SearchUsecase;
import com.bjcareer.search.domain.entity.Stock;
import com.bjcareer.search.domain.entity.Thema;
import com.bjcareer.search.domain.gpt.GPTStockNewsDomain;
import com.bjcareer.search.domain.gpt.thema.GPTThemaNewsDomain;
import com.bjcareer.search.in.api.controller.dto.QueryToFindRaiseReasonResponseDTO;
import com.bjcareer.search.in.api.controller.dto.QueryToFindThemaNewsResponseDTO;
import com.bjcareer.search.in.api.controller.dto.SearchResultResponseDTO;
import com.bjcareer.search.in.api.controller.dto.StockInformationResponseDTO;
import com.bjcareer.search.in.api.controller.dto.StockerFilterResultResponseDTO;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v0/search")
public class SearchController {
	private final SearchUsecase usecase;

	@GetMapping("/thema")
	@Operation(summary = "테마 검색 결과 조회", description = "사용자가 요청한 검색어를 기반으로 검색된 결과를 Return합니다.")
	public ResponseEntity<SearchResultResponseDTO> filterThemesByQuery(@RequestParam(name = "q") String query) {
		if (validationKeyword(query)) {
			return ResponseEntity.badRequest().build();
		}

		List<Thema> searchResult = usecase.filterThemesByQuery(query);
		return ResponseEntity.ok(new SearchResultResponseDTO(query, searchResult));
	}

	@GetMapping("/stock")
	@Operation(summary = "주식 검색 결과 조회", description = "사용자가 요청한 검색어를 기반으로 검색된 결과를 Return합니다.")
	public ResponseEntity<StockerFilterResultResponseDTO> filterStocksByQuery(@RequestParam(name = "q") String query) {
		if (validationKeyword(query)) {
			return ResponseEntity.badRequest().build();
		}
		List<Stock> searchResult = usecase.filterStockByQuery(query.toUpperCase());
		StockerFilterResultResponseDTO response = new StockerFilterResultResponseDTO(query, searchResult);

		return ResponseEntity.ok(response);
	}

	@GetMapping("/news/thema")
	@Operation(summary = "테마 뉴스 조회", description = "사용자가 요청한 검색어를 기반으로 검색된 결과를 Return합니다.")
	public ResponseEntity<QueryToFindThemaNewsResponseDTO> filterThemaNewsByQuery(
		@RequestParam(name = "q") String code,
		@RequestParam(name = "theme", required = false, defaultValue = "ALL") String theme,
		@RequestParam(name = "date", required = false) LocalDate date) {

		if (validationKeyword(code)) {
			return ResponseEntity.badRequest().build();
		}

		log.debug("request news-thema: {} {} {}", code, theme, date);
		Pair<List<String>, List<GPTThemaNewsDomain>> themasNews = usecase.findThemasNews(code, theme, date);
		return ResponseEntity.ok(new QueryToFindThemaNewsResponseDTO(themasNews.getSecond(), themasNews.getFirst()));
	}

	@GetMapping("/raise-reason")
	@Operation(
		summary = "요청한 날짜에 해당하는 주식의 상승 이유 조회",
		description = "쿼리파람의 dater가 있으면 특정 날짜의 주식 상승 이유를 조회하는 API입니다. 없다면 최근 일자부터 상승 이유를 반환."
	)
	public ResponseEntity<QueryToFindRaiseReasonResponseDTO> filterStocksByQuery(
		@RequestParam(name = "q") String code, @RequestParam(name = "date", required = false) LocalDate date) {

		log.debug("request raise-reason: {} {}", code, date);

		if (validationKeyword(code)) {
			return ResponseEntity.badRequest().build();
		}

		List<GPTStockNewsDomain> contents = usecase.findRaiseReason(code, date);
		QueryToFindRaiseReasonResponseDTO response = new QueryToFindRaiseReasonResponseDTO(
			contents);

		return ResponseEntity.ok(response);
	}

	private boolean validationKeyword(String query) {
		return query == null || query.isEmpty();
	}
}
