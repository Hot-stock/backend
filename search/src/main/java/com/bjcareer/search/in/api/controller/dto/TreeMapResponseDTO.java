package com.bjcareer.search.in.api.controller.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.bjcareer.search.domain.TreeMapDomain;

import lombok.Getter;

@Getter
public class TreeMapResponseDTO {
	private final String name;
	private final Double value;
	private final List<Content> children = new ArrayList<>();

	public TreeMapResponseDTO(TreeMapDomain domain) {
		name = domain.getThemaName();
		Map<String, Double> stockIncreaseRate =
			domain.getStockIncreaseRate();

		stockIncreaseRate.keySet()
			.forEach(stock -> {
				children.add(new Content(stock, String.format("%.2f", stockIncreaseRate.get(stock))));
			});

		value = domain.getValue();
	}

	@Getter
	private static class Content {
		private final String name;
		private final String value;

		public Content(String name, String value) {
			this.name = name;
			this.value = value;
		}
	}
}
