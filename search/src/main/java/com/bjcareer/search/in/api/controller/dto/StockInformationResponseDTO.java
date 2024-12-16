package com.bjcareer.search.in.api.controller.dto;

import com.bjcareer.search.domain.entity.Stock;

import lombok.Getter;

@Getter
public class StockInformationResponseDTO {
	private final String stockName;
	private final String stockCode;
	private final String logoUrl;
	private final String soarRate;

	public StockInformationResponseDTO(Stock stock, String soarRate) {
		this.stockName = stock.getName();
		this.stockCode = stock.getCode();
		this.logoUrl = stock.getPreSignedURL();
		this.soarRate = soarRate;
	}
}
