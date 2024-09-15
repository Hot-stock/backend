package com.bjcareer.search.controller.dto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.bjcareer.search.domain.entity.Thema;
import com.bjcareer.search.domain.entity.ThemaInfo;

import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
public class SearchThemaResponseDTO {
	Map<String, List<ThemaStock>> res = new HashMap<>();

	public SearchThemaResponseDTO(List<ThemaInfo> themas) {

		for (ThemaInfo t : themas) {
			for (Thema s : t.getThemas()) {
				res.putIfAbsent(t.getName(), new ArrayList<>());
				res.get(t.getName()).add(new ThemaStock(s.getStock().getName(), s.getStock().getHref(), s.getStock().getMarketCapitalization()));
			}
		}
	}

	@Getter
	static class ThemaStock{
		private String name;
		private String marketCap;
		private String href;

		public ThemaStock(String name, String href, Long marketCap) {
			this.name = name;
			this.href = href;
			marketCap = marketCap/100000000L;
			this.marketCap = marketCap + "억";
		}
	}
}
