package com.bjcareer.search.out.persistence.noSQL;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.bjcareer.search.domain.gpt.insight.GPTInsight;

@SpringBootTest
class DocumentAnalyzeInsightRepositoryTest {
	@Autowired
	DocumentAnalyzeInsightRepository documentAnalyzeInsightRepository;

	@Test
	void 찿아진_데이터가_있어야_함() {
		String stockName = "컬러레이";
		Optional<GPTInsight> result = documentAnalyzeInsightRepository.getInsightOfStockByLatest(stockName);
		assertTrue(result.isPresent());
	}

	@Test
	void 찾아진_데이터가_없어서_empty_반환(){
		String stockName = "컬러레이2";
		Optional<GPTInsight> result = documentAnalyzeInsightRepository.getInsightOfStockByLatest(stockName);
		assertFalse(result.isPresent());
	}
}
