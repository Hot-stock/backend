package com.bjcareer.GPTService.out.persistence.document;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Locale;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.bjcareer.GPTService.domain.gpt.GPTNewsDomain;
import com.bjcareer.GPTService.domain.gpt.OriginalNews;

@SpringBootTest
class GPTStockNewsRepositoryTest {
	@Autowired
	GPTStockNewsRepository gptStockNewsRepository;

	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE, dd MMM yyyy HH:mm:ss VV", Locale.ENGLISH);
	LocalDateTime now = LocalDateTime.now();
	String date = now.atZone(ZoneId.of("GMT")).format(formatter);

	@Test
	void 저장테스트() {
		OriginalNews originalNews = new OriginalNews("배럴", "더위가 심해지면서", "2021-07-01", "더위가 심해지면서", date, "더위가 심해지면서");
		GPTNewsDomain gptNewsDomain = new GPTNewsDomain("배럴", "더위", new ArrayList<>(), "2021-07-01", "더위가 심해지면서",
			originalNews);
		GPTNewsDomain saved = gptStockNewsRepository.save(gptNewsDomain);
		assertNotNull(saved);
	}

	@Test
	void 링크를_이용해서_저장된_데이터_조회() {
		String link = "www.naver.com";
		OriginalNews originalNews = new OriginalNews("배럴", link, link, "더위가 심해지면서", date, "더위가 심해지면서");
		GPTNewsDomain gptNewsDomain = new GPTNewsDomain("배럴", "더위", new ArrayList<>(), "2021-07-01", "더위가 심해지면서",
			originalNews);

		GPTNewsDomain saved = gptStockNewsRepository.save(gptNewsDomain);
		assertEquals(saved, gptStockNewsRepository.findByLink(link));
	}

	@Test
	void 중복_저장_테스트() {
		String link = "www.naver.com";
		OriginalNews originalNews = new OriginalNews("배럴", link, link, "더위가 심해지면서", date, "더위가 심해지면서");
		GPTNewsDomain gptNewsDomain = new GPTNewsDomain("배럴", "더위", new ArrayList<>(), "2021-07-01", "더위가 심해지면서",
			originalNews);

		GPTNewsDomain saved = gptStockNewsRepository.save(gptNewsDomain);
		assertEquals(saved, gptStockNewsRepository.save(saved));
	}
}
