package com.bjcareer.search.out.api.python;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.bjcareer.search.application.port.out.api.NewsCommand;
import com.bjcareer.search.domain.News;

@SpringBootTest
class PythonSearchServerAdapterTest {

	@Autowired
	PythonSearchServerAdapter pythonSearchServerAdapter;

	@Test
	void 특정일의_뉴스를_가지고_오는지_테스트() {
		// given
		LocalDate baseDate = LocalDate.of(2018, 11, 8);
		NewsCommand command = new NewsCommand("진성티이씨", baseDate, baseDate);
		List<News> news = pythonSearchServerAdapter.fetchNews(command);
		assertNotNull(news);
	}
}
