package com.bjcareer.GPTService.application;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;

import com.bjcareer.GPTService.TestUtil;
import com.bjcareer.GPTService.domain.gpt.GPTNewsDomain;
import com.bjcareer.GPTService.domain.gpt.OriginalNews;

class AnalyzeBestNewsTest {

	@Test
	void 정해진조건에_따라서_최선의_뉴스가_선택되는지() {
		// given
		AnalyzeBestNews analyzeBestNews = new AnalyzeBestNews();
		OriginalNews originalNews = new OriginalNews("배럴", "fakeLink", "img_fake", TestUtil.PUB_DATE, "더위가 심해지면서");

		// when
		Optional<GPTNewsDomain> bestNews = analyzeBestNews.getBestNews(List.of(
			new GPTNewsDomain("배럴", "", null, "",  originalNews),
			new GPTNewsDomain("배럴", "이유1", null, "",  originalNews),
			new GPTNewsDomain("배럴", "이유2", null, "다음이유1", originalNews)
		));

		// then
		assertEquals("이유2", bestNews.get());
	}
}
