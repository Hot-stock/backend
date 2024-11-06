package com.bjcareer.search.IntegrationTest.application.search;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.bjcareer.search.application.search.ConverterKeywordCountService;
import com.bjcareer.search.domain.AbsoluteRankKeyword;

@SpringBootTest
class ConverterKeywordCountServiceTest {
	@Autowired
	ConverterKeywordCountService converterKeywordCountService;

	@Test
	void 주어진_키워드로_검색어를_반환하지는지() {
		//given
		String keyword = "테스트";
		//when
		List<AbsoluteRankKeyword> absoluteValueOfKeyword = converterKeywordCountService.getAbsoluteValueOfKeyword(
			keyword);

		//then
		assertFalse(absoluteValueOfKeyword.isEmpty());
	}

}
