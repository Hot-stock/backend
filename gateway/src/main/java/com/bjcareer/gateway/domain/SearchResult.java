package com.bjcareer.gateway.domain;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import lombok.Getter;

@Getter
@Data
public class SearchResult {
	private String keyword;
	private List<StockDTO> results;

	@Getter
	@Data
	static class StockDTO {
		private String name;
		private String href;
		private String marketCap;
		private List<String> themes = new ArrayList<>();
	}

}

