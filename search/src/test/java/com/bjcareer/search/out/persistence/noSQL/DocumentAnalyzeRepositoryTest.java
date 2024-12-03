package com.bjcareer.search.out.persistence.noSQL;

import java.time.LocalDate;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.bjcareer.search.config.AppConfig;
import com.bjcareer.search.domain.gpt.GPTNewsDomain;

@SpringBootTest
class DocumentAnalyzeRepositoryTest {
	@Autowired
	DocumentAnalyzeRepository documentAnalyzeRepository;

	@Test
	void 오늘_날짜_이후에_진행되는_뉴스들을_반환() {
		List<GPTNewsDomain> upcomingNews = documentAnalyzeRepository.getUpcomingNews();


		for(GPTNewsDomain gptNewsDomain : upcomingNews){
			System.out.println("gptNewsDomain.getNews().getTitle() = " + gptNewsDomain.getStockName());
			assertTrue(gptNewsDomain.getNext().isPresent());
			assertTrue(LocalDate.now(AppConfig.ZONE_ID).isBefore(gptNewsDomain.getNext().get()));

			System.out.println("gptNewsDomain.getReason() = " + gptNewsDomain.getNextReason());
			System.out.println("gptNewsDomain.getNews().getTitle() = " + gptNewsDomain.getNews().getTitle());
		}

	}

}
