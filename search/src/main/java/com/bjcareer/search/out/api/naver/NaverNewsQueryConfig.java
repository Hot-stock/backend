package com.bjcareer.search.out.api.naver;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class NaverNewsQueryConfig {
	private final String keyword;
	private final int display;
	private int startIdx = 0;
	private final NaverNewsSort sort;

	public String buildUrl(String baseUrl) {
		startIdx += 1;
		String encode = URLEncoder.encode(keyword, StandardCharsets.UTF_8);

		return String.format("%s?display=%d&start=%s&sort=%s&query=%s", baseUrl, display, startIdx,
			sort.getValue(),
			keyword);
	}
}
