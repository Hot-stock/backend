package com.bjcareer.search.out.persistence.noSQL;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.bjcareer.search.application.port.out.persistence.stock.LoadStockRaiseReason;
import com.bjcareer.search.application.port.out.persistence.thema.LoadThemaNewsCommand;
import com.bjcareer.search.config.AppConfig;
import com.bjcareer.search.domain.gpt.GPTNewsDomain;
import com.bjcareer.search.domain.gpt.thema.GPTThema;

@SpringBootTest
class DocumentAnalyzeRepositoryTest {
	@Autowired
	DocumentAnalyzeRepository documentAnalyzeRepository;

	@Test
	void 오늘_날짜_이후에_진행되는_뉴스들을_반환() {
		List<GPTNewsDomain> upcomingNews = documentAnalyzeRepository.getUpcomingNews();

		for(GPTNewsDomain gptNewsDomain : upcomingNews){
			assertTrue(gptNewsDomain.getNext().isPresent());
			assertTrue(LocalDate.now(AppConfig.ZONE_ID).isBefore(gptNewsDomain.getNext().get()));
		}
	}

	@Test
	void 특정_주식의_상승_이유를_요청() {
		String target = "와이즈버즈";
		LoadStockRaiseReason command = new LoadStockRaiseReason(target, null);
		List<GPTNewsDomain> reason = documentAnalyzeRepository.getRaiseReason(command);

		for (GPTNewsDomain gptNewsDomain : reason) {
			assertEquals(gptNewsDomain.getStockName(), target);
		}
	}

	@Test
	void 특정_날짜의_주식의_상승_이유를_요청() {
		String target = "에이텍";
		LocalDate date = LocalDate.of(2024,12,12);
		LoadStockRaiseReason command = new LoadStockRaiseReason(target, date);
		List<GPTNewsDomain> reason = documentAnalyzeRepository.getRaiseReason(command);

		System.out.println("reason = " + reason);

		for (GPTNewsDomain gptNewsDomain : reason) {
			assertEquals(gptNewsDomain.getStockName(), target);
			assertEquals(gptNewsDomain.getNews().getPubDate(), date);
		}
	}

	@Test
	void 테마이름을_찾을_수_있는지_체크() {
		String target = "이재명";
		LoadThemaNewsCommand command = new LoadThemaNewsCommand(target, null);
		List<GPTThema> reason = documentAnalyzeRepository.getThemaNews(command);

		for (GPTThema thema : reason) {
			assertEquals(target, thema.getName(), "테마 이름이 일치하지 않습니다.");
		}
	}

}
