package com.bjcareer.search.in.api.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.util.Pair;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bjcareer.search.application.information.HotTopicService;
import com.bjcareer.search.application.port.in.RankingUsecase;
import com.bjcareer.search.domain.entity.Stock;
import com.bjcareer.search.domain.gpt.GPTStockNewsDomain;
import com.bjcareer.search.domain.gpt.insight.GPTInsight;
import com.bjcareer.search.in.api.controller.dto.QueryStockNewsResponseDTO;
import com.bjcareer.search.in.api.controller.dto.StockInformationResponseDTO;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/v0/ranking")
@RequiredArgsConstructor
@Slf4j
public class RankingController {
	private final RankingUsecase usecase;
	private final HotTopicService hotTopicService;

	@GetMapping("/keywords")
	@Operation(summary = "랭킹 조회 기능", description = "사용자의 검색어 랭킹을 조회할 수 있습니다")
	public ResponseEntity<Map<String, Object>> getRanking(
		@RequestParam(name = "q", required = false, defaultValue = "10") Integer index) {
		List<Pair<Stock, String>> rankKeyword = usecase.getRankKeyword(index);
		List<StockInformationResponseDTO> response = rankKeyword.stream()
			.map(it -> new StockInformationResponseDTO(it.getFirst(), it.getSecond()))
			.toList();

		Map<String, Object> map = new HashMap<>();

		map.put("total", response.size());
		map.put("items", response);

		return ResponseEntity.ok(map);
	}

	@GetMapping("/stocks")
	@Operation(summary = "HotTopic 조회", description = "현재 상승률이 가장 높은 10종목(코스피, 코스닥)의 이류를 반환합니다. 다만 반듯이 이류를 찾는 것이 아니기 때문에 총 20개가 안될 수도 있습니다.")
	public ResponseEntity<QueryStockNewsResponseDTO> getHotTopic() {
		log.info("Request hot topic");
		List<GPTStockNewsDomain> hotTopics = hotTopicService.getTrendingStory();
		QueryStockNewsResponseDTO responseDTO = new QueryStockNewsResponseDTO(hotTopics);
		return ResponseEntity.ok(responseDTO);
	}

	@GetMapping("/suggestion")
	@Operation(summary = "추전 주식 종목 조회", description = "키워드 검색어 기반으로 추천된 종목을 반환함")
	public ResponseEntity<Map<String, Object>> getSuggestionStocks() {
		List<GPTInsight> suggestionStocks = usecase.getSuggestionStocks();
		List<StockInformationResponseDTO> response = suggestionStocks.stream()
			.map(StockInformationResponseDTO::new)
			.toList();

		Map<String, Object> map = new HashMap<>();

		map.put("total", response.size());
		map.put("items", response);

		return ResponseEntity.ok(map);
	}


}
