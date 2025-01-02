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
import com.bjcareer.search.domain.PaginationDomain;
import com.bjcareer.search.domain.gpt.GPTStockNewsDomain;
import com.bjcareer.search.domain.gpt.thema.GPTThemaNewsDomain;

@SpringBootTest
class DocumentAnalyzeRepositoryTest {
	@Autowired
	DocumentAnalyzeRepository documentAnalyzeRepository;

	@Test
	void 오늘_날짜_이후에_진행되는_뉴스들을_반환() {
		PaginationDomain<GPTStockNewsDomain> upcomingNews = documentAnalyzeRepository.getUpcomingNews(1, 5);
		System.out.println("upcomingNews = " + upcomingNews.getContent());
		// for(GPTStockNewsDomain gptStockNewsDomain : upcomingNews){
		// 	assertTrue(gptStockNewsDomain.getNext().isPresent());
		// 	assertTrue(LocalDate.now(AppConfig.ZONE_ID).isBefore(gptStockNewsDomain.getNext().get()));
		// }
	}

	@Test
	void 날짜복원테test(){
		String link = "http://www.econonews.co.kr/news/articleView.html?idxno=369243";
		List<GPTStockNewsDomain> stockNewsByLinks = documentAnalyzeRepository.getStockNewsByLinks(List.of(link));
		GPTStockNewsDomain first = stockNewsByLinks.getFirst();

		assertEquals(LocalDate.of(2025, 1, 2), first.getNews().getPubDate());
	}

	@Test
	void 범위쿼리테스트(){
		List<GPTStockNewsDomain> stockNewsByLinks = documentAnalyzeRepository.getRaiseReason(new LoadStockRaiseReason("동신건설", LocalDate.of(2025, 1, 2)));
		GPTStockNewsDomain first = stockNewsByLinks.getFirst();
		assertEquals(LocalDate.of(2025, 1, 2), first.getNews().getPubDate());
	}

	@Test
	void 특정_주식의_상승_이유를_요청() {
		String target = "신원";
		LoadStockRaiseReason command = new LoadStockRaiseReason(target, null);
		List<GPTStockNewsDomain> reason = documentAnalyzeRepository.getRaiseReason(command);

		for (GPTStockNewsDomain gptStockNewsDomain : reason) {
			System.out.println("gptStockNewsDomain.getNews().getPubDate() = " + gptStockNewsDomain.getNews().getPubDate());
			assertEquals(gptStockNewsDomain.getStockName(), target);
		}
	}

	@Test
	void 특정_날짜의_주식의_상승_이유를_요청() {
		String target = "에이텍";
		LocalDate date = LocalDate.of(2024,12,12);
		LoadStockRaiseReason command = new LoadStockRaiseReason(target, date);
		List<GPTStockNewsDomain> reason = documentAnalyzeRepository.getRaiseReason(command);

		System.out.println("reason = " + reason);

		for (GPTStockNewsDomain gptStockNewsDomain : reason) {
			assertEquals(gptStockNewsDomain.getStockName(), target);
			assertEquals(gptStockNewsDomain.getNews().getPubDate(), date);
		}
	}

	@Test
	void 테마이름을_찾을_수_있는지_체크() {
		String target = "이재명";
		LoadThemaNewsCommand command = new LoadThemaNewsCommand(target, null);
		List<GPTThemaNewsDomain> reason = documentAnalyzeRepository.getThemaNews(command);

		for (GPTThemaNewsDomain thema : reason) {
			assertEquals(target, thema.getName(), "테마 이름이 일치하지 않습니다.");
		}
	}

	@Test
	void 뉴스가_가지고_있는_테마_찾기() {
		String id = "http://www.thebigdata.co.kr/view.php?ud=202412160514188315cd1e7f0bdf_23";
		String stockName = "효성오앤비";

		List<String> themasOfNews = documentAnalyzeRepository.getThemasOfNews(id, stockName);
		System.out.println("themasOfNews = " + themasOfNews);
	}

}
