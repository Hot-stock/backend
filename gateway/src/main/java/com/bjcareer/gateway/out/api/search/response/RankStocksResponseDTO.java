package com.bjcareer.gateway.out.api.search.response;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;

@Getter
public class RankStocksResponseDTO {
	private int total;
	private List<StockInformationDTO> items;


	@Getter
	@Setter
	@JsonIgnoreProperties
	public static class StockInformationDTO {
		private String stockName;
		private String stockCode;
		private String logoUrl;
		private String soarRate;
		private String reasonDetail;
	}
}
