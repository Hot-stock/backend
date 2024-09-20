package com.bjcareer.search.controller.dto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.bjcareer.search.domain.entity.Thema;

import lombok.Getter;

public class SearchStockResponseDTO {
	public List<StockThemaResponse> responses = new ArrayList<>();
	private Map<String, StockThemaResponse> res = new HashMap<>();

	public SearchStockResponseDTO(List<Thema> themas) {
		for (Thema t : themas) {
			String stockName = t.getStock().getName();

			res.putIfAbsent(stockName, new StockThemaResponse(stockName, new ArrayList<>()));
			res.get(stockName).getThemas().add(t.getThemaInfo().getName());
		}
		responses = res.values().stream().toList();
	}

	@Getter
	static class StockThemaResponse {
		private String stock;
		private List<String> themas;

		public StockThemaResponse(String stock, List<String> themas) {
			this.stock = stock;
			this.themas = themas;
		}
	}
}
