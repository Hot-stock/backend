package com.bjcareer.GPTService.out.api.gpt.thema.orgingalNews;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
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
	void 뉴스_필터링_테스트() {
		String link = "https://www.widedaily.com/news/articleView.html?idxno=253555";
		String thema = "중국 경기부양책";
		OriginalNews originalNews = pythonSearchServerAdapter.fetchNewsBody(link, LocalDate.now()).get();
		Optional<GPTThema> gptThema = gptThemaAdapter.summaryThemaNews(originalNews, thema, GPTThemaAdapter.GPT_4o);
		assertFalse(gptThema.get().isRelatedThema());
	}
}
