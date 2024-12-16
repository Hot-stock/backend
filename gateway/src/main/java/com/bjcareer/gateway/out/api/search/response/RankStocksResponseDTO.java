package com.bjcareer.gateway.out.api.search.response;

import java.util.List;

import lombok.Getter;

@Getter
public class RankStocksResponseDTO {
	private int total;
	private List<StockInformationDTO> items;


	@Getter
	public static class StockInformationDTO {
		private String stockName;
		private String stockCode;
		private String logoUrl;
		private String soarRate;
	}
}
