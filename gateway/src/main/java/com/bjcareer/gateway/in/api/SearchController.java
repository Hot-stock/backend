package com.bjcareer.gateway.in.api;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bjcareer.gateway.aop.APILimit.APIRateLimit;
import com.bjcareer.gateway.application.ports.out.KeywordCommand;
import com.bjcareer.gateway.application.ports.out.KeywordServerPort;
import com.bjcareer.gateway.application.ports.out.LoadRaiseReasonOfStock;
import com.bjcareer.gateway.application.ports.out.LoadThemaNews;
import com.bjcareer.gateway.application.ports.out.SearchServerPort;
import com.bjcareer.gateway.domain.AbsoluteRankKeyword;
import com.bjcareer.gateway.domain.ResponseDomain;
import com.bjcareer.gateway.domain.SearchResult;
import com.bjcareer.gateway.in.api.response.KeywordCountResponseDTO;
import com.bjcareer.gateway.out.api.search.response.RaiseReasonResponseDTO;
import com.bjcareer.gateway.out.api.search.response.StockerFilterResultResponseDTO;
import com.bjcareer.gateway.out.api.search.response.ThemaNewsResponseDTO;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@Slf4j
public class SearchController {
	private final SearchServerPort searchServerPort;

	// @APIRateLimit
	@GetMapping("/api/v0/search/thema")
	@Operation(summary = "검색 결과 조회", description = "사용자가 요청한 검색어를 기반으로 검색된 결과를 Return합니다.")
	public ResponseEntity<ResponseDomain<SearchResult>> searchResult(@RequestParam(name = "q") String query,
		HttpServletRequest request) {
		log.info("Request query: {}", query);
		if (validationKeyword(query)) {
			log.debug("Request query is empty");
			return ResponseEntity.badRequest().build();
		}

		SearchResult result = searchServerPort.searchResult(new KeywordCommand(query));
		return new ResponseEntity<>(new ResponseDomain<>(HttpStatus.OK, result, null), HttpStatus.OK);
	}

	@GetMapping("/api/v0/search/stock")
	@Operation(summary = "검색 결과 조회", description = "사용자가 요청한 검색어를 기반으로 검색된 결과를 Return합니다.")
	public ResponseEntity<ResponseDomain<StockerFilterResultResponseDTO>> filterStocksByQuery(
		@RequestParam(name = "q") String query,
		HttpServletRequest request) {
		log.info("Request search query: {}", query);
		if (validationKeyword(query)) {
			log.debug("Request query is empty");
			return ResponseEntity.badRequest().build();
		}

		ResponseDomain<StockerFilterResultResponseDTO> res = searchServerPort.filterStockByQuery(
			new KeywordCommand(query));
		return new ResponseEntity<>(res, res.getStatusCode());
	}

	@GetMapping("/api/v0/search/raise-reason")
	@Operation(
		summary = "요청한 날짜에 해당하는 주식의 상승 이유 조회",
		description = "쿼리파람의 dater가 있으면 특정 날짜의 주식 상승 이유를 조회하는 API입니다. 없다면 최근 일자부터 상승 이유를 반환."
	)
	public ResponseEntity<ResponseDomain<RaiseReasonResponseDTO>> findRaiseReason(
		@RequestParam(name = "q") String query, @RequestParam(name = "date", required = false) LocalDate date,
		HttpServletRequest request) {

		log.info("Request query: {} {}", query, date);
		LoadRaiseReasonOfStock loadRaiseReasonOfStock = new LoadRaiseReasonOfStock(query, date);
		ResponseDomain<RaiseReasonResponseDTO> res = searchServerPort.findRaiseReasonOfStock(
			loadRaiseReasonOfStock);

		return new ResponseEntity<>(res, res.getStatusCode());
	}

	@GetMapping("/api/v0/search/news/thema")
	@Operation(summary = "테마 뉴스 조회", description = "사용자가 요청한 테마를 기반으로 검색된 뉴스를 돌려줍니다.")
	public ResponseEntity<ResponseDomain<ThemaNewsResponseDTO>> searchThemaNews(@RequestParam(name = "q") String code,
		@RequestParam(name = "date", required = false) LocalDate date,
		@RequestParam(name = "theme", required = false, defaultValue = "ALL") String theme, HttpServletRequest request) {
		log.info("Request query: {} {} {}", code, date, theme);

		if (validationKeyword(code)) {
			log.debug("Request query is empty");
			return ResponseEntity.badRequest().build();
		}

		ResponseDomain<ThemaNewsResponseDTO> res = searchServerPort.findThemaNews(new LoadThemaNews(code, theme, date));
		return new ResponseEntity<>(res, res.getStatusCode());
	}

	private boolean validationKeyword(String query) {
		return query == null || query.isEmpty();
	}

}
