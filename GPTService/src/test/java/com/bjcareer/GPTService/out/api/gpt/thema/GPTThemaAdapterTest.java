package com.bjcareer.GPTService.out.api.gpt.thema;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.bjcareer.GPTService.domain.gpt.OriginalNews;
import com.bjcareer.GPTService.domain.gpt.thema.GPTThema;
import com.bjcareer.GPTService.out.api.gpt.thema.orgingalNews.GPTThemaAdapter;
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
			"https://www.edaily.co.kr/News/Read?newsId=01784326639117208&mediaCodeNo=257", null);

		Optional<GPTThema> gptThema = gptThemaAdapter.summaryThemaNews(originalNews.get(), "");
		assertTrue(gptThema.isPresent());
	}

}
