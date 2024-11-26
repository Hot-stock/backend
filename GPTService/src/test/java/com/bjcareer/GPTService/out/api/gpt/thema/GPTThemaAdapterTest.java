package com.bjcareer.GPTService.out.api.gpt.thema;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.bjcareer.GPTService.domain.gpt.OriginalNews;
import com.bjcareer.GPTService.domain.gpt.thema.GPTThema;
import com.bjcareer.GPTService.out.api.python.PythonSearchServerAdapter;

@SpringBootTest
class GPTThemaAdapterTest {

	@Autowired
	private GPTThemaAdapter gptThemaAdapter;

	@Autowired
	private PythonSearchServerAdapter pythonSearchServerAdapter;

	@Test
	void test() {
		Optional<OriginalNews> originalNews = pythonSearchServerAdapter.fetchNewsBody(
			"https://www.widedaily.com/news/articleView.html?idxno=252027");

		Optional<GPTThema> gptThema = gptThemaAdapter.summaryThemaNews(originalNews.get(), "우크라이나 재건");
		assertTrue(gptThema.isPresent());
	}

}
