package com.bjcareer.search.out.api.gpt.thema;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.bjcareer.search.application.port.out.api.NewsCommand;
import com.bjcareer.search.domain.GPTThema;
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
		String keyword = "우크라이나 재건";
		LocalDate startDate = LocalDate.of(2024, 11, 8);
		NewsCommand newsCommand = new NewsCommand("우크라이나 재건", startDate, startDate);
		List<News> news = pythonSearchServerAdapter.fetchNews(newsCommand);
		List<GPTThema> themaList = new ArrayList<>();

		for (News n : news) {
			GPTThema thema = chatGPTThemaAdapter.summaryThemaNews(n.getContent(), "우크라이나 재건", startDate);
			themaList.add(thema);
			break;
		}

		ThemaInfo themaInfo = themaInfoRepository.findByName(keyword).get();
		themaInfo.addThemaNews(themaList);
	}

}
