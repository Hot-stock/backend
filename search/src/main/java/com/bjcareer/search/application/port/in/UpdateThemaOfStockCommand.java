package com.bjcareer.search.application.port.in;

import java.util.List;

import lombok.Getter;

@Getter
public class UpdateThemaOfStockCommand {
	private List<String> stockName;
	private String name;

	public UpdateThemaOfStockCommand(List<String> stockName, String name) {
		this.stockName = stockName;
		this.name = name;
	}
}
