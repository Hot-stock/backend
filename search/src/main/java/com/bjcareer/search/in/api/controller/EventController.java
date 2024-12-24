package com.bjcareer.search.in.api.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bjcareer.search.application.information.NextEventService;
import com.bjcareer.search.domain.PaginationDomain;
import com.bjcareer.search.domain.gpt.GPTStockNewsDomain;
import com.bjcareer.search.in.api.controller.dto.PageResponseDTO;
import com.bjcareer.search.in.api.controller.dto.QueryStockNewsResponseDTO;
import com.bjcareer.search.in.api.controller.dto.StockNewsResponseDTO;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/v0/event")
@RequiredArgsConstructor
public class EventController {
	private final NextEventService eventService;

	@GetMapping("/schedule")
	@Operation(summary = "다가오는 일정 모두 조회", description = "오늘 날짜를 기준으로 앞으로 남은 일정들을 모두 조회")
	public ResponseEntity<PageResponseDTO<StockNewsResponseDTO>> getUpcomingEvent(@RequestParam int page, @RequestParam int size) {
		PaginationDomain<GPTStockNewsDomain> upcomingEvents = eventService.getUpcomingEvents(page, size);
		List<StockNewsResponseDTO> contents = upcomingEvents.getContent().stream().map(StockNewsResponseDTO::new).toList();
		PageResponseDTO<StockNewsResponseDTO> res = new PageResponseDTO<>(contents,
			upcomingEvents.getTotalElements(), upcomingEvents.getCurrentPage(), upcomingEvents.getPageSize());
		return ResponseEntity.ok(res);
	}

	@GetMapping("/next-schedule")
	@Operation(summary = "이 주식의 다음 일정을 요청함", description = "주식 이름으로 나온 뉴스 기사를 종합해서 다음 일정을 파악함")
	public ResponseEntity<QueryStockNewsResponseDTO> searchNextSchedule(
		@RequestParam(name = "q") String code) {
		log.debug("request next-schedule: {}", code);

		List<GPTStockNewsDomain> nextSchedule = eventService.filterUpcomingEventsByStockName(code);
		QueryStockNewsResponseDTO responseDTO = new QueryStockNewsResponseDTO(nextSchedule);
		return new ResponseEntity<>(responseDTO, HttpStatus.OK);
	}
}
