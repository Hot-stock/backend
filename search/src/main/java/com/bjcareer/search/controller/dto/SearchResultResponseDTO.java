package com.bjcareer.search.controller.dto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.bjcareer.search.domain.entity.Thema;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;

@Getter
public class SearchResultResponseDTO {
	private String keyword;
	private List<StockDTO> results = new ArrayList<>();

	@JsonIgnore
	private Map<String, Boolean> map = new HashMap<>();

	public SearchResultResponseDTO(String keyword, List<Thema> resultOfSearch) {
		this.keyword = keyword;

		resultOfSearch.forEach(result -> {

			if (map.getOrDefault(result.getStock().getName(), false)) {
				return;
			}

			map.put(result.getStock().getName(), true);

			results.add(new StockDTO(result.getStock().getName(), result.getStock().getHref(),
				result.getStock().getMarketCapitalization()));
		});
	}

	@Getter
	static class StockDTO {
		private String name;
		private String href;
		private String marketCap;

		private static final long ONE_TRILLION = 1_000_000_000_000L; // 1조
		private static final long ONE_HUNDRED_MILLION = 100_000_000L; // 1억

		public StockDTO(String name, String href, Long marketCap) {
			this.name = name;
			this.href = href;
			this.marketCap = convertMarketCap(marketCap);
		}

		// marketCap 값을 조 단위와 억 단위로 변환하여 출력하는 메서드
		private String convertMarketCap(Long marketCap) {
			long trillions = marketCap / ONE_TRILLION; // 조 단위
			long remainingAfterTrillion = marketCap % ONE_TRILLION; // 조 단위 계산 후 남은 값
			long hundredMillions = remainingAfterTrillion / ONE_HUNDRED_MILLION; // 억 단위

			if (trillions > 0) {
				if (hundredMillions > 0) {
					return String.format("%d조 %d억", trillions, hundredMillions);
				} else {
					return String.format("%d조", trillions);
				}
			} else {
				return String.format("%d억", hundredMillions);
			}
		}
	}

}
