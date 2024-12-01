package com.bjcareer.GPTService.out.api.toss;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class SoarStockRequestDTO {
	private String id = "heavy_soar";
	private String duration = "1d";
	private List<String> filters = new ArrayList<>();
	private String tag = "kr";

	public SoarStockRequestDTO() {
		// Initialize default filters
		this.filters.add("MARKET_CAP_GREATER_THAN_50M");
		this.filters.add("STOCKS_PRICE_GREATER_THAN_ONE_DOLLAR");
		this.filters.add("KRX_MANAGEMENT_STOCK");
	}
}
