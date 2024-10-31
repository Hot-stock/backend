package com.bjcareer.search.out.api.python;

import java.time.LocalDate;
import java.time.ZoneId;

public class OHLCQueryConfig {
	private final String code;
	private final String startDay;
	private final String endDay;
	ZoneId seoul = ZoneId.of("Asia/Seoul");

	public OHLCQueryConfig(String code) {
		this.code = code;
		this.startDay = LocalDate.now(seoul).minusDays(1).toString();
		this.endDay = LocalDate.now(seoul).toString();
	}

	public OHLCQueryConfig(String code, boolean allData) {
		this.code = code;
		this.startDay = "1999-01-01";
		this.endDay = LocalDate.now(seoul).toString();
	}

	public String buildUrl(String baseUrl) {
		return String.format("%s?code=%s&'startDay'=%s&'endDay'=%s", baseUrl, code, startDay, endDay);
	}

}
