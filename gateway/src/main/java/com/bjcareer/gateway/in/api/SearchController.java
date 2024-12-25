package com.bjcareer.gateway.in.api;

import java.time.LocalDate;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bjcareer.gateway.application.ports.out.KeywordCommand;
import com.bjcareer.gateway.application.ports.out.LoadRaiseReasonOfStock;
import com.bjcareer.gateway.application.ports.out.LoadThemaNews;
import com.bjcareer.gateway.application.ports.out.SearchServerPort;
import com.bjcareer.gateway.domain.ResponseDomain;
import com.bjcareer.gateway.in.api.response.TreeMapResponseDTO;
import com.bjcareer.gateway.out.api.search.response.GPTAnalayzeThemaNewsResponseDTO;
import com.bjcareer.gateway.out.api.search.response.PageResponseDTO;
import com.bjcareer.gateway.out.api.search.response.RaiseReasonResponseDTO;
import com.bjcareer.gateway.out.api.search.response.StockerFilterResultResponseDTO;
import com.bjcareer.gateway.out.api.search.response.ThemaNamesResponseDTO;
import com.bjcareer.gateway.out.api.search.response.ThemaNewsOfStockResponseDTO;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@Slf4j
public class SearchController {
	private final SearchServerPort searchServerPort;

	// // @APIRateLimit
	// @GetMapping("/api/v0/search/thema")
	// @Operation(summary = "검색 결과 조회", description = "사용자가 요청한 검색어를 기반으로 검색된 결과를 Return합니다.")
	// public ResponseEntity<ResponseDomain<SearchResult>> searchResult(@RequestParam(name = "q") String query,
	// 	HttpServletRequest request) {
	// 	log.info("Request query: {}", query);
	// 	if (validationKeyword(query)) {
	// 		log.debug("Request query is empty");
	// 		return ResponseEntity.badRequest().build();
	// 	}
	//
	// 	SearchResult result = searchServerPort.searchResult(new KeywordCommand(query));
	// 	return new ResponseEntity<>(new ResponseDomain<>(HttpStatus.OK, result, null), HttpStatus.OK);
	// }

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
	public ResponseEntity<ResponseDomain<ThemaNewsOfStockResponseDTO>> searchThemaNews(
		@RequestParam(name = "q") String code,
		@RequestParam(name = "date", required = false) LocalDate date,
		@RequestParam(name = "theme", required = false, defaultValue = "ALL") String theme, HttpServletRequest request) {
		log.info("Request query: {} {} {}", code, date, theme);

		if (validationKeyword(code)) {
			log.debug("Request query is empty");
			return ResponseEntity.badRequest().build();
		}

		ResponseDomain<ThemaNewsOfStockResponseDTO> res = searchServerPort.findThemaNews(
			new LoadThemaNews(code, theme, date));
		return new ResponseEntity<>(res, res.getStatusCode());
	}

	@GetMapping("/api/v0/markets/visualization")
	@Operation(summary = "테마 뉴스 조회", description = "사용자가 요청한 테마를 기반으로 검색된 뉴스를 돌려줍니다.")
	public ResponseEntity<ResponseDomain<List<TreeMapResponseDTO>>> searchTreeMap() {
		log.info("Request tree map");
		ResponseDomain<List<TreeMapResponseDTO>> res = searchServerPort.loadTreeMap();
		return new ResponseEntity<>(res, res.getStatusCode());
	}

	@GetMapping("/api/v0/themas")
	@Operation(summary = "저장되어 있는 테마명을 모두 로딩", description = "저장되어 있는 테마명을 모두 로딩합니다.")
	public ResponseEntity<ResponseDomain<ThemaNamesResponseDTO>> getThemaInfo() {
		log.info("Request themas");
		ResponseDomain<ThemaNamesResponseDTO> res = searchServerPort.loadThemaNames();
		return new ResponseEntity<>(res, res.getStatusCode());
	}

	@GetMapping("/api/v0/themas/{id}/news")
	@Operation(
		summary = "테마에 속한 뉴스 반환",
		description = "특정 테마 ID에 속하는 뉴스들을 최신순으로 반환합니다. 페이지 번호와 페이지 크기를 설정할 수 있습니다.",
		parameters = {
			@Parameter(name = "id", description = "테마 ID", required = true),
			@Parameter(name = "page", description = "페이지 번호 (0부터 시작)", example = "0", required = false),
			@Parameter(name = "size", description = "페이지 크기", example = "10", required = false)
		}
	)
	public ResponseEntity<ResponseDomain<PageResponseDTO<GPTAnalayzeThemaNewsResponseDTO>>> loadThemaNews(
		@PathVariable("id") Integer id, @RequestParam(value = "page", defaultValue = "1") int page,
		@RequestParam(value = "size", defaultValue = "2") int size){
		log.info("Request thema news: {} {} {}", id, page, size);
		ResponseDomain<PageResponseDTO<GPTAnalayzeThemaNewsResponseDTO>> res = searchServerPort.loadThemaNews(id, page, size);
		return new ResponseEntity<>(res, res.getStatusCode());
	}


	private boolean validationKeyword(String query) {
		return query == null || query.isEmpty();
	}

}
