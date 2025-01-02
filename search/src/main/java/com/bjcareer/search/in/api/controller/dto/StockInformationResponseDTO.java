package com.bjcareer.search.in.api.controller.dto;

import com.bjcareer.search.domain.entity.Stock;
import com.bjcareer.search.domain.gpt.insight.GPTInsight;

import lombok.Getter;

@Getter
public class StockInformationResponseDTO {
	private final String stockName;
	private final String stockCode;
	private final String logoUrl;
	private final String soarRate;
	private final String reasonDetail;

	public StockInformationResponseDTO(Stock stock, String score) {
		this.stockName = stock.getName();
		this.stockCode = stock.getCode();
		this.logoUrl = stock.getPreSignedURL();
		this.soarRate = score;
		this.reasonDetail = "";
	}

	public StockInformationResponseDTO(GPTInsight insight) {
		this.stockName = insight.getStockName();
		this.stockCode = insight.getStock().getCode();
		this.logoUrl = insight.getStock().getPreSignedURL();
		this.soarRate = insight.getScore().toString();
		this.reasonDetail = insight.getReason() + "\n" + insight.getReasonDetail();
	}
}
