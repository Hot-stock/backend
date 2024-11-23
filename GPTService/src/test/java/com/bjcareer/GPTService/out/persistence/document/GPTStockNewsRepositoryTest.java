package com.bjcareer.GPTService.out.persistence.document;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;

import com.bjcareer.GPTService.TestUtil;
import com.bjcareer.GPTService.domain.gpt.GPTNewsDomain;
import com.bjcareer.GPTService.domain.gpt.OriginalNews;

@DataMongoTest
class GPTStockNewsRepositoryTest {
	@Autowired
	GPTStockNewsRepository gptStockNewsRepository;

	@Test
	void 저장테스트() {
		OriginalNews originalNews = new OriginalNews("배럴", "fakeLink", "img_fake", TestUtil.PUB_DATE, "더위가 심해지면서");
		GPTNewsDomain gptNewsDomain = new GPTNewsDomain("배럴", "더위", new ArrayList<>(), "2021-07-01", "더위가 심해지면서",
			originalNews);
		GPTNewsDomain saved = gptStockNewsRepository.save(gptNewsDomain);
		assertNotNull(saved);
	}

	@Test
	void 링크를_이용해서_저장된_데이터_조회() {
		String link = "www.naver.com";

		OriginalNews originalNews = new OriginalNews("배럴", link, "img_fake", TestUtil.PUB_DATE, "더위가 심해지면서");
		GPTNewsDomain gptNewsDomain = new GPTNewsDomain("배럴", "더위", new ArrayList<>(), "2021-07-01", "더위가 심해지면서",
			originalNews);

		gptStockNewsRepository.save(gptNewsDomain);
		assertTrue(gptStockNewsRepository.findByLink(link).isPresent());
	}

	@Test
	void 중복_저장_테스트() {
		String link = "www.naver.com";
		OriginalNews originalNews = new OriginalNews("배럴", "fakeLink", "img_fake", TestUtil.PUB_DATE, "더위가 심해지면서");
		GPTNewsDomain gptNewsDomain = new GPTNewsDomain("배럴", "더위", new ArrayList<>(), "2021-07-01", "더위가 심해지면서",
			originalNews);

		GPTNewsDomain saved = gptStockNewsRepository.save(gptNewsDomain);
		assertEquals(saved, gptStockNewsRepository.save(saved));
	}

	@Test
	void 찾고자하는_객체가_없을_때() {
		Optional<GPTNewsDomain> byLink = gptStockNewsRepository.findByLink("www.naver.com1");
		assertEquals(Optional.empty(), byLink);
	}
}
