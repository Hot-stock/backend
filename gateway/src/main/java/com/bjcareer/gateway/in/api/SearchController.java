package com.bjcareer.gateway.in.api;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bjcareer.gateway.aop.APILimit.APIRateLimit;
import com.bjcareer.gateway.application.ports.in.KeywordUsecase;
import com.bjcareer.gateway.application.ports.out.KeywordCommand;
import com.bjcareer.gateway.application.ports.out.SearchServerPort;
import com.bjcareer.gateway.domain.AbsoluteRankKeyword;
import com.bjcareer.gateway.domain.SearchCandidate;
import com.bjcareer.gateway.domain.SearchResult;
import com.bjcareer.gateway.in.api.response.KeywordCountResponseDTO;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@Slf4j
public class SearchController {
	private final KeywordUsecase keywordUsecase;
	private final SearchServerPort searchServerPort;

	@GetMapping("/api/v0/keyword")
	@APIRateLimit
	@Operation(
		summary = "키워드 검색 통계 조회",
		description = "요청한 키워드들의 검색 건수를 집계하여 반환하는 API입니다. " +
			"외부 검색 API와 통신하여 데이터를 수집하며, 각 키워드의 검색 수를 계산하여 제공합니다." +
			"따라서 느릴 수 있습니다" +
			"유료 결제 회원만 사용 가능합니다"
	)
	public ResponseEntity<List<KeywordCountResponseDTO>> getKeywordCount(@RequestParam(name = "q") String keyword,
		HttpServletRequest request) {
		List<AbsoluteRankKeyword> searchCount = keywordUsecase.getSearchCount(keyword);

		List<KeywordCountResponseDTO> absoluteValueOfKeyword = searchCount.stream()
			.map(it -> new KeywordCountResponseDTO(it.getAbsoluteKeywordCount(), it.getPeriod()))
			.collect(Collectors.toList());

		return new ResponseEntity<>(absoluteValueOfKeyword, HttpStatus.OK);
	}

	@GetMapping("/api/v0/search")
	@APIRateLimit
	@Operation(summary = "검색어 후보 기능", description = "사용자가 검색을 할 때, 검색어를 입력하면 검색어 후보를 Return합니다.")
	public ResponseEntity<SearchCandidate> search(@RequestParam(name = "q") String query, HttpServletRequest request) {
		if (validationKeyword(query)) {
			return ResponseEntity.badRequest().build();
		}

		SearchCandidate searchCandidate = searchServerPort.searchCandidate(new KeywordCommand(query));
		return new ResponseEntity<>(searchCandidate, HttpStatus.OK);
	}

	@GetMapping("/api/v0/sr")
	@APIRateLimit
	@Operation(summary = "검색 결과 조회", description = "사용자가 요청한 검색어를 기반으로 검색된 결과를 Return합니다.")
	public ResponseEntity<SearchResult> searchResult(@RequestParam(name = "q") String query,
		HttpServletRequest request) {
		if (validationKeyword(query)) {
			return ResponseEntity.badRequest().build();
		}

		SearchResult result = searchServerPort.searchResult(new KeywordCommand(query));
		return ResponseEntity.ok(result);
	}

	private boolean validationKeyword(String query) {
		return query == null || query.isEmpty();
	}

}
