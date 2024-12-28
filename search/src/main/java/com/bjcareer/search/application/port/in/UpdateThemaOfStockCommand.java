package com.bjcareer.search.application.port.in;

import java.util.List;

import lombok.Getter;

@Getter
public class UpdateThemaOfStockCommand {
	private List<String> stockName;
	private String reason;
	private String name;

	public UpdateThemaOfStockCommand(List<String> stockName, String name, String reason) {
		this.stockName = stockName;
		this.reason = reason;
		this.name = name;
	}
}
