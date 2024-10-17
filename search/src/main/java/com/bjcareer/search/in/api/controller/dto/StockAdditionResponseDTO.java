package com.bjcareer.search.in.api.controller.dto;

import lombok.Data;

@Data
public class StockAdditionResponseDTO {
	private Long id;
	private String stockName;
	private String theme;
	private String code;

	public StockAdditionResponseDTO(Long id, String stockName, String theme, String code) {
		this.id = id;
		this.stockName = stockName;
		this.theme = theme;
		this.code = code;
	}
}
