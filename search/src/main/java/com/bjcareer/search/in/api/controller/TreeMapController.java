package com.bjcareer.search.in.api.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bjcareer.search.application.TreeMapService;
import com.bjcareer.search.domain.TreeMapDomain;
import com.bjcareer.search.in.api.controller.dto.TreeMapResponseDTO;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v0/search/treemap")
@RequiredArgsConstructor
public class TreeMapController {
	private final TreeMapService treeMapService;
	private final Integer performance = 3;

	@GetMapping
	public ResponseEntity<List<TreeMapResponseDTO>> getTreeMap() {
		List<TreeMapResponseDTO> response = new ArrayList<>();
		List<TreeMapDomain> domains = treeMapService.calcHitMap(performance);
		domains.sort((a, b) -> b.getValue().compareTo(a.getValue()));
		domains.stream().map(TreeMapResponseDTO::new).forEach(response::add);
		return ResponseEntity.ok(response);
	}

}
