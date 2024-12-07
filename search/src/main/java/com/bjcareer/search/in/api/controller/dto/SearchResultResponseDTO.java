package com.bjcareer.search.in.api.controller.dto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.bjcareer.search.domain.entity.Thema;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;

@Getter
public class SearchResultResponseDTO {
	private final String keyword;
	private final List<StockDTO> items;

	@JsonIgnore
	private final Map<String, StockDTO> map = new HashMap<>();

	public SearchResultResponseDTO(String keyword, List<Thema> resultOfSearch) {
		this.keyword = keyword;

		resultOfSearch.forEach(result -> {
			if (map.containsKey(result.getStock().getName())) {
				map.get(result.getStock().getName()).addTheme(result.getThemaInfo().getName());
				return;
			}

			StockDTO stockDTO = new StockDTO(result.getStock().getName(), result.getStock().getHref(),
				result.getStock().getMarketCapitalization(), result.getThemaInfo().getName());
			map.put(result.getStock().getName(), stockDTO);
		});

		items = map.values().stream().toList();
	}

	@Getter
	static class StockDTO {
		private final String name;
		private final String href;
		private final String marketCap;
		private final List<String> themes = new ArrayList<>();

		private static final long ONE_TRILLION = 1_000_000_000_000L; // 1조
		private static final long ONE_HUNDRED_MILLION = 100_000_000L; // 1억

		public StockDTO(String name, String href, Long marketCap, String theme) {
			this.name = name;
			this.href = href;
			this.marketCap = convertMarketCap(marketCap);
			addTheme(theme);
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

		private void addTheme(String theme) {
			if (themes.contains(theme)) {
				return;
			}

			themes.add(theme);
		}

		@Override
		public boolean equals(Object object) {
			if (this == object)
				return true;
			if (object == null || getClass() != object.getClass())
				return false;
			StockDTO stockDTO = (StockDTO)object;
			return Objects.equals(name, stockDTO.name) && Objects.equals(href, stockDTO.href);
		}

		@Override
		public int hashCode() {
			return Objects.hash(name, href);
		}
	}

}
