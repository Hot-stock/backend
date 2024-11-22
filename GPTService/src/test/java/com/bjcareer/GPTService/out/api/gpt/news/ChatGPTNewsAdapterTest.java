package com.bjcareer.GPTService.out.api.gpt.news;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.bjcareer.GPTService.domain.gpt.GPTNewsDomain;
import com.bjcareer.GPTService.domain.gpt.OriginalNews;

@SpringBootTest
class ChatGPTNewsAdapterTest {
	@Autowired
	GPTNewsAdapter gptNewsAdapter;

	@Test
	void test() {
		OriginalNews originalNews = new OriginalNews("배럴", "더위가 심해지면서", "2021-07-01", "더위가 심해지면서", "2021-07-01",
			"더위가 심해지면서");
		Optional<GPTNewsDomain> opt = gptNewsAdapter.findStockRaiseReason(originalNews, "테스트를 위한 메세지입니다.", LocalDate.now());
		assertNotNull(opt);
	}
}
