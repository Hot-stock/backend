package com.bjcareer.search.in.api.controller;

import static java.util.Collections.*;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.ResponseEntity;
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

	@GetMapping
	public ResponseEntity<List<TreeMapResponseDTO>> getTreeMap() {
		List<TreeMapResponseDTO> response = new ArrayList<>();
		List<TreeMapDomain> domains = treeMapService.loadTreeMap();
		if (domains.isEmpty()) {
			return ResponseEntity.ok(emptyList());
		}
		domains.stream().map(TreeMapResponseDTO::new).forEach(response::add);
		return ResponseEntity.ok(response);
	}

}
