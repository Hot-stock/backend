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

		// 우크라이나 제건 -> 배경적 맥락은 정치아닌가?
		AnalyzeThemaNewsCommand command = new AnalyzeThemaNewsCommand("우크라이나", LocalDate.now().minusYears(1));
		List<GPTThema> gptThemas = gptThemaNewsAnalyzeService.analyzeThemaNewsByNewsLink(command);
	}

}
