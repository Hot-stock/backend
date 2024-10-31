package com.bjcareer.search.out.api.naver;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class NaverNewsQueryConfigTest {

	@Test
	void query_url_build() {
		String answer = "https://openapi.naver.com/v1/search/news.json?display=10&start=1&sort=date&query=%EA%B7%B8%EB%A6%B0%EB%A6%AC%EC%86%8C%EC%8A%A4";

		NaverNewsQueryConfig config = new NaverNewsQueryConfig("그린리소스", 10, NaverNewsSort.DATE);
		String url = config.buildUrl("https://openapi.naver.com/v1/search/news.json");

		assertEquals(answer, url);
	}

}
