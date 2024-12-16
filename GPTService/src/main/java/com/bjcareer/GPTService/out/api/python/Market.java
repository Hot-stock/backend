package com.bjcareer.GPTService.out.api.python;

import java.util.HashMap;
import java.util.Map;

public enum Market {
	KOSPI, KOSDAQ, NASDAQ;

	private static final Map<String, Market> MAPPING = new HashMap<>();

	static {
		MAPPING.put("KOSPI", KOSPI);
		MAPPING.put("KOSDAQ", KOSDAQ);
		MAPPING.put("NASDAQ", NASDAQ);
		MAPPING.put("KSQ", KOSDAQ); // 추가 매핑
		MAPPING.put("STK", KOSPI); // 추가 매핑
	}

	public static Market fromString(String value) {
		Market market = MAPPING.get(value.toUpperCase());
		if (market == null) {
			throw new IllegalArgumentException("Invalid Market value: " + value);
		}
		return market;
	}
}
