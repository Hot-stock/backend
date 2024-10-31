package com.bjcareer.search.out.api.naver;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.bjcareer.search.domain.News;

@SpringBootTest
class ApiNaverNewsTest {
	@Autowired
	ApiNaverNews apiNaverNews;

	@Test
	void 네이버_뉴스_api_테스트() {
		NaverNewsQueryConfig config = new NaverNewsQueryConfig("진양산업", 100, NaverNewsSort.SIM);
		List<News> news = apiNaverNews.fetchNews(config);

		assertEquals(100, news.size());

		for (News n : news) {
			System.out.println("n.getTitle() = " + n.getTitle());
			System.out.println("n.getPubDate() = " + n.getPubDate());
		}

	}

}
