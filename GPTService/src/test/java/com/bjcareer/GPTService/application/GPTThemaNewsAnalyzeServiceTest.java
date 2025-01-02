package com.bjcareer.GPTService.application;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.bjcareer.GPTService.application.port.in.AnalyzeThemaNewsCommand;
import com.bjcareer.GPTService.domain.gpt.thema.GPTThema;

@SpringBootTest
class GPTThemaNewsAnalyzeServiceTest {
	@Autowired
	GPTThemaNewsAnalyzeService gptThemaNewsAnalyzeService;

	@Test
	void test() {
		AnalyzeThemaNewsCommand command = new AnalyzeThemaNewsCommand("우크라이나", LocalDate.now().minusYears(1));
		List<GPTThema> gptThemas = gptThemaNewsAnalyzeService.analyzeThemaNewsByNewsLink(command);
	}

	@Test
	void 저장된_데이터를_찾을_수_있는지(){
		List<GPTThema> themaNews = gptThemaNewsAnalyzeService.getAnalyzeThemaNews("경기 부양책", LocalDate.now().minusDays(3), LocalDate.now());
		assertEquals(3, themaNews.size());
	}

}
