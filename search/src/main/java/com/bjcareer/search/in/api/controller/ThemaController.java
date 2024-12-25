package com.bjcareer.search.in.api.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bjcareer.search.application.port.out.persistence.thema.LoadThemaNewsCommand;
import com.bjcareer.search.application.thema.ThemaService;
import com.bjcareer.search.domain.PaginationDomain;
import com.bjcareer.search.domain.entity.ThemaInfo;
import com.bjcareer.search.domain.gpt.thema.GPTThemaNewsDomain;
import com.bjcareer.search.in.api.controller.dto.GPTAnalayzeThemaNewsResponseDTO;
import com.bjcareer.search.in.api.controller.dto.LoadThemaNameResDTO;
import com.bjcareer.search.in.api.controller.dto.PageResponseDTO;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v0/thema")
@Slf4j
public class ThemaController {
	private final ThemaService themaService;

	@GetMapping
	@Operation(summary = "모든 테마 로딩", description = "DB에 저장된 테마 정보들 로드")
	public ResponseEntity<LoadThemaNameResDTO> loadThemas() {
		List<ThemaInfo> themaInfos = themaService.loadThemaName();
		LoadThemaNameResDTO loadThemaNameResDTO = new LoadThemaNameResDTO(themaInfos);
		return ResponseEntity.ok(loadThemaNameResDTO);
	}

	@GetMapping("/news")
	@Operation(summary = "특정 테마의 뉴스 요청", description = "특정 테마의 정보 요청")
	public ResponseEntity<PageResponseDTO<GPTAnalayzeThemaNewsResponseDTO>> loadThemasByQuery(@RequestParam(name = "q") String query, @RequestParam(name = "page", defaultValue = "1") int page, @RequestParam(name = "size", defaultValue = "2") int size) {
		PaginationDomain<GPTThemaNewsDomain> gptThemaNewsDomainPaginationDomain = themaService.loadThemaNewsByQuery(
			new LoadThemaNewsCommand(query, page, size));

		List<GPTAnalayzeThemaNewsResponseDTO> contents = gptThemaNewsDomainPaginationDomain.getContent()
			.stream()
			.map(GPTAnalayzeThemaNewsResponseDTO::new)
			.toList();

		PageResponseDTO<GPTAnalayzeThemaNewsResponseDTO> responseDTO = new PageResponseDTO<>(contents, gptThemaNewsDomainPaginationDomain.getTotalElements(), page, size);

		return ResponseEntity.ok(responseDTO);
	}
}
