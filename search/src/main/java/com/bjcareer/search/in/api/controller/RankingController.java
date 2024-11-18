package com.bjcareer.search.in.api.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bjcareer.search.application.information.HotTopicService;
import com.bjcareer.search.application.port.in.RankingUsecase;
import com.bjcareer.search.domain.gpt.GTPNewsDomain;
import com.bjcareer.search.in.api.controller.dto.QueryToFindRaiseReasonResponseDTO;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v0/ranking")
@RequiredArgsConstructor
public class RankingController {
	private final RankingUsecase usecase;
	private final HotTopicService hotTopicService;

	@GetMapping
	@Operation(summary = "랭킹 조회 기능", description = "사용자의 검색어 랭킹을 조회할 수 있습니다" + "지금은 폴링이지만 나중에는 웹소켓으로 연동 가능.")
	public ResponseEntity<Map<String, List<String>>> getRanking(
		@RequestParam(name = "q", required = false, defaultValue = "10") Integer index) {
		List<String> rankKeyword = usecase.getRankKeyword(index);
		Map<String, List<String>> response = Map.of("rank", rankKeyword);
		return ResponseEntity.ok(response);
	}

	@GetMapping("/stocks")
	@Operation(summary = "HotTopic 조회", description = "현재 상승률이 가장 높은 10종목(코스피, 코스닥)의 이류를 반환합니다. 다만 반듯이 이류를 찾는 것이 아니기 때문에 총 20개가 안될 수도 있습니다.")
	public ResponseEntity<QueryToFindRaiseReasonResponseDTO> getHotTopic() {
		List<GTPNewsDomain> hotTopics = hotTopicService.getTrendingStory();

		if (hotTopics.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

		QueryToFindRaiseReasonResponseDTO responseDTO = new QueryToFindRaiseReasonResponseDTO(hotTopics);

		return ResponseEntity.ok(responseDTO);
	}

}
