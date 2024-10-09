package com.bjcareer.search.out.api.naver;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.bjcareer.search.domain.NewsDomain;

@SpringBootTest
class ApiNaverNewsTest {
	@Autowired
	ApiNaverNews apiNaverNews;

	@Test
	void test() {
		List<NewsDomain> result = apiNaverNews.fetchNews("그린리소스");
		NewsDomain newsDomain = result.get(0);
		Optional<String> content = newsDomain.getContent();
		assertNotNull(content.get());
	}

}
