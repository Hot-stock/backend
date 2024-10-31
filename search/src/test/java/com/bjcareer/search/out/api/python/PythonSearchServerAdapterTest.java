package com.bjcareer.search.out.api.python;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.bjcareer.search.domain.News;

@SpringBootTest
class PythonSearchServerAdapterTest {
	@Autowired
	PythonSearchServerAdapter pythonSearchServerAdapter;

	@Test
	void test() {
		News news = new News("title", "https://view.asiae.co.kr/article/2024103111415543401", "https://view.asiae.co.kr/article/2024103111415543401", "description", "Tue, 31 Oct 2023 15:30:00 +0000");
		pythonSearchServerAdapter.getNewsBody(news);
	}
}
