package com.bjcareer.search.out.api.gpt.thema;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.bjcareer.search.application.port.out.api.NewsCommand;
import com.bjcareer.search.domain.gpt.thema.GPTThema;
import com.bjcareer.search.domain.News;
import com.bjcareer.search.domain.entity.ThemaInfo;
import com.bjcareer.search.out.api.python.PythonSearchServerAdapter;
import com.bjcareer.search.out.persistence.repository.thema.ThemaInfoRepository;

@SpringBootTest
class ChatGPTThemaAdapterTest {
	@Autowired
	private PythonSearchServerAdapter pythonSearchServerAdapter;

	@Autowired
	private ChatGPTThemaAdapter chatGPTThemaAdapter;

	@Autowired
	private ThemaInfoRepository themaInfoRepository;

	@Test
	@Transactional
	void saveThemaInfo() {
		ThemaInfo themaInfo = new ThemaInfo("우크라이나 재건", "");
		themaInfoRepository.save(themaInfo);
	}

	@Test
	void test() {
		String keyword = "희토류";
		LocalDate startDate = LocalDate.of(2020, 3, 20);
		NewsCommand newsCommand = new NewsCommand(keyword, startDate, startDate);
		List<News> news = pythonSearchServerAdapter.fetchNews(newsCommand);
		List<GPTThema> themaList = new ArrayList<>();

		System.out.println("news.size() = " + news.size());

		for (News n : news) {
			Optional<GPTThema> gptThema = chatGPTThemaAdapter.summaryThemaNews(n.getContent(), keyword, startDate);
		}

		// ThemaInfo themaInfo = themaInfoRepository.findByName(keyword).get();
		// themaInfo.addThemaNews(themaList);
	}

}
