package com.bjcareer.GPTService.out.api.gpt.news;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.bjcareer.GPTService.domain.gpt.GPTNewsDomain;
import com.bjcareer.GPTService.domain.gpt.OriginalNews;
import com.bjcareer.GPTService.out.api.python.PythonSearchServerAdapter;
import com.bjcareer.GPTService.out.persistence.document.GPTStockNewsRepository;

@SpringBootTest
class GPTNewsAdapterTest {
	@Autowired
	GPTNewsAdapter gptNewsAdapter;

	@Autowired
	PythonSearchServerAdapter pythonSearchServerAdapter;
	// https://biz.heraldcorp.com/article/10010105?ref=naver

	@Test
	void test() {
		OriginalNews originalNews = pythonSearchServerAdapter.fetchNewsBody(
			"https://biz.heraldcorp.com/article/10010105?ref=naver", LocalDate.now()).get();
		Optional<GPTNewsDomain> opt = gptNewsAdapter.findStockRaiseReason(originalNews, "오리엔트정공", LocalDate.now());
		assertNotNull(opt);
	}
}
