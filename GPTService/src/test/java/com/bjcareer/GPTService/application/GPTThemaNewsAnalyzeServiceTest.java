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
		AnalyzeThemaNewsCommand command = new AnalyzeThemaNewsCommand("우크라이나", LocalDate.now().minusDays(1));

		List<GPTThema> gptThemas = gptThemaNewsAnalyzeService.analyzeThemaNewsByNewsLink(command);

		System.out.println("gptThemas = " + gptThemas);
	}

}
