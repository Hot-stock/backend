package com.bjcareer.GPTService.out.api.python;

import lombok.Data;

@Data
public class MarketResponseDTO {
	private String name;
	private String symbol;
	private String market;
	private int price;
	private Long cap;
}
