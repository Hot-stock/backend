package com.bjcareer.search.out.api.naver;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class NaverNewsQueryConfig {
	private final String keyword;
	private final int display;
	private int startIdx = 0;
	private final NaverNewsSort sort;

	public String buildUrl(String baseUrl) {
		startIdx += 1;
		return String.format("%s?display=%d&start=%s&sort=%s&query=%s", baseUrl, display, startIdx,
			sort.getValue(),
			keyword);
	}
}
