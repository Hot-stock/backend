package com.bjcareer.search.in.api.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bjcareer.search.application.information.NextEventService;
import com.bjcareer.search.domain.gpt.GPTNewsDomain;
import com.bjcareer.search.in.api.controller.dto.QueryToFindNextEventReasonResponseDTO;
import com.bjcareer.search.in.api.controller.dto.QueryToFindRaiseReasonResponseDTO;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v0/event")
@RequiredArgsConstructor
public class EventController {
	private final NextEventService eventService;

	@GetMapping
	@Operation(
		summary = "다가오는 일정 조회",
		description = "오늘 날짜를 기준으로 앞으로 남은 일정들을 모두 조회"
	)
	public ResponseEntity<QueryToFindNextEventReasonResponseDTO> getUpcomingEvent() {
		List<GPTNewsDomain> upcomingEvents = eventService.getUpcomingEvents();
		QueryToFindNextEventReasonResponseDTO queryToFindRaiseReasonResponseDTO = new QueryToFindNextEventReasonResponseDTO(
			upcomingEvents);

		return ResponseEntity.ok(queryToFindRaiseReasonResponseDTO);
	}
}
