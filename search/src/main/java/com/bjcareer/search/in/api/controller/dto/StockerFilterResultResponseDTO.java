package com.bjcareer.search.in.api.controller.dto;

import java.util.ArrayList;
import java.util.List;

import com.bjcareer.search.domain.entity.Stock;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class StockerFilterResultResponseDTO {
	private final String keyword;
	private final List<Content> items = new ArrayList<>();

	public StockerFilterResultResponseDTO(String keyword, List<Stock> stocks) {
		this.keyword = keyword;

		for (Stock stock : stocks) {
			List<String> themas = stock.getThemas().stream().map(t -> t.getThemaInfo().getName()).toList();
			Content content = new Content(stock.getName(), stock.getCode(), themas);
			this.items.add(content);
		}
	}

	@Getter
	static class Content {
		private final String name;
		private final String code;
		private final List<String> themes;

		public Content(String name, String code, List<String> theme) {
			this.name = name;
			this.code = code;
			this.themes = theme;
		}
	}
}
