package com.bjcareer.search.out.api.gpt.thema;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.bjcareer.search.application.port.out.api.NewsCommand;
import com.bjcareer.search.config.AppConfig;
import com.bjcareer.search.domain.News;
import com.bjcareer.search.domain.gpt.thema.GPTThema;
import com.bjcareer.search.out.api.python.PythonSearchServerAdapter;

@SpringBootTest
class ChatGPTThemaAdapterTest {
	@Autowired
	private PythonSearchServerAdapter pythonSearchServerAdapter;

	@Autowired
	private ChatGPTThemaAdapter chatGPTThemaAdapter;

	@Test
	void test() {
		String keyword = "오징어게임";
		LocalDate startDate = LocalDate.now(AppConfig.ZONE_ID).minusDays(1);
		NewsCommand newsCommand = new NewsCommand(keyword, startDate, startDate);
		List<News> news = pythonSearchServerAdapter.fetchNews(newsCommand);

		for (News n : news) {
			Optional<GPTThema> gptThema = chatGPTThemaAdapter.summaryThemaNews(n, keyword);
			if (gptThema.isPresent()) {
				System.out.println("gptThema = " + gptThema.get());
			}
		}

		// ThemaInfo themaInfo = themaInfoRepository.findByName(keyword).get();
		// themaInfo.addThemaNews(themaList);
	}

}
