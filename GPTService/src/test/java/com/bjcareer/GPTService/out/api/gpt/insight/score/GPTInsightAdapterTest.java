package com.bjcareer.GPTService.out.api.gpt.insight.score;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.bjcareer.GPTService.application.AnalyzeInsightKeyword;
import com.bjcareer.GPTService.domain.gpt.insight.GPTInsight;

@SpringBootTest
class GPTInsightAdapterTest {
	@Autowired
	AnalyzeInsightKeyword analyzeInsightKeyword;



	@Test
	void test() {
		LocalDate startDate = LocalDate.now().minusDays(7);
		LocalDate endDate = LocalDate.now();
		String stockName = "컬러레이";
		GPTInsight gptInsight = analyzeInsightKeyword.analyzeInsightKeyword(stockName, startDate, endDate);
		System.out.println(gptInsight);
	}
}
