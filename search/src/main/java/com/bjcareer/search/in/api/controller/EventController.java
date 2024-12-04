package com.bjcareer.search.in.api.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bjcareer.search.application.information.NextEventService;
import com.bjcareer.search.domain.gpt.GPTNewsDomain;
import com.bjcareer.search.in.api.controller.dto.QueryToFindNextEventReasonResponseDTO;
import com.bjcareer.search.in.api.controller.dto.QueryToFindRaiseReasonResponseDTO;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/v0/event")
@RequiredArgsConstructor
public class EventController {
	private final NextEventService eventService;

	@GetMapping
	@Operation(
		summary = "다가오는 일정 모두 조회",
		description = "오늘 날짜를 기준으로 앞으로 남은 일정들을 모두 조회"
	)
	public ResponseEntity<QueryToFindNextEventReasonResponseDTO> getUpcomingEvent() {
		List<GPTNewsDomain> upcomingEvents = eventService.getUpcomingEvents();
		QueryToFindNextEventReasonResponseDTO queryToFindRaiseReasonResponseDTO = new QueryToFindNextEventReasonResponseDTO(
			upcomingEvents);

		return ResponseEntity.ok(queryToFindRaiseReasonResponseDTO);
	}

	@GetMapping("/next-schedule")
	@Operation(summary = "이 주식의 다음 일정을 요청함ㄹㄷ?", description = "주식 이름으로 나온 뉴스 기사를 종합해서 다음 일정을 파악함")
	public ResponseEntity<QueryToFindNextEventReasonResponseDTO> searchNextSchedule(
		@RequestParam(name = "q") String stockName) {
		log.debug("request next-schedule: {}", stockName);

		List<GPTNewsDomain> nextSchedule = eventService.filterUpcomingEventsByStockName(stockName);
		QueryToFindNextEventReasonResponseDTO responseDTO = new QueryToFindNextEventReasonResponseDTO(
			nextSchedule);

		return new ResponseEntity<>(responseDTO, HttpStatus.OK);
	}
}
