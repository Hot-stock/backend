package com.bjcareer.search.out.api.naver;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class NaverNewsQueryConfigTest {

	@Test
	void query_url_build() {
		String keyword = "그린리소스";
		String answer = "https://openapi.naver.com/v1/search/news.json?display=10&start=1&sort=date&query=" + keyword;

		NaverNewsQueryConfig config = new NaverNewsQueryConfig(keyword, 10, NaverNewsSort.DATE);
		String url = config.buildUrl("https://openapi.naver.com/v1/search/news.json");

		assertEquals(answer, url);
	}

}
