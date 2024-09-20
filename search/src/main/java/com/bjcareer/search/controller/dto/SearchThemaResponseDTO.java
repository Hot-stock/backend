package com.bjcareer.search.controller.dto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.bjcareer.search.domain.entity.Thema;
import com.bjcareer.search.domain.entity.ThemaInfo;

import lombok.Getter;

public class SearchThemaResponseDTO {
	public List<ThemaResponse> response = new ArrayList<>();

	public SearchThemaResponseDTO(List<ThemaInfo> themas) {
		// Loop over each ThemaInfo object
		for (ThemaInfo t : themas) {
			ThemaResponse themaResponse = new ThemaResponse(t.getName());

			// For each theme, loop through its associated stocks (Themas)
			for (Thema s : t.getThemas()) {
				ThemaStock stock = new ThemaStock(
					s.getStock().getName(),
					s.getStock().getHref(),
					s.getStock().getMarketCapitalization()
				);
				themaResponse.getStocks().add(stock);
			}

			response.add(themaResponse);
		}
	}

	@Getter
	static class ThemaResponse {
		private String thema;
		private List<ThemaStock> stocks = new ArrayList<>();

		public ThemaResponse(String thema) {
			this.thema = thema;
		}
	}

	@Getter
	static class ThemaStock {
		private String name;
		private String marketCap;
		private String href;

		public ThemaStock(String name, String href, Long marketCap) {
			this.name = name;
			this.href = href;

			// Convert marketCap to billions and append "억"
			this.marketCap = (marketCap / 100000000L) + "억";
		}
	}
}
