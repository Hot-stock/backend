package com.bjcareer.search.controller;

import java.util.List;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bjcareer.search.domain.AbsoluteRankKeyword;
import com.bjcareer.search.event.SearchedKeyword;
import com.bjcareer.search.service.ConverterSearchCountService;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v0/keyword")
@RequiredArgsConstructor
public class KeywordController {
	private final ConverterSearchCountService converterSearchCountService;
	private final ApplicationEventPublisher applicationEventPublisher;

	@GetMapping
	@Operation(
		summary = "키워드 검색 통계 조회",
		description = "요청한 키워드들의 검색 건수를 집계하여 반환하는 API입니다. " +
			"외부 검색 API와 통신하여 데이터를 수집하며, 각 키워드의 검색 수를 계산하여 제공합니다." +
			"따라서 느릴 수 있습니다" +
			"유료 결제 회원만 사용 가능합니다"
	)
	public ResponseEntity<List<AbsoluteRankKeyword>> getSearchCount(@RequestParam(name = "q") String keyword) {
		applicationEventPublisher.publishEvent(new SearchedKeyword(keyword));
		List<AbsoluteRankKeyword> absoluteValueOfKeyword = converterSearchCountService.getAbsoluteValueOfKeyword(
			keyword);
		return ResponseEntity.ok(absoluteValueOfKeyword);
	}
}
