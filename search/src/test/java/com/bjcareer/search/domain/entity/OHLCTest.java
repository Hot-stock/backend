package com.bjcareer.search.domain.entity;

import static org.junit.jupiter.api.Assertions.*;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import com.bjcareer.search.domain.GTPNewsDomain;
import com.bjcareer.search.domain.News;

@ExtendWith(MockitoExtension.class)
class OHLCTest {

	private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE, dd MMM yyyy HH:mm:ss VV",
		Locale.ENGLISH);
	private final String pubDate = ZonedDateTime.now().format(formatter);
	private final News news = new News("title", "link", "link", "묘사", pubDate, "휴가로 인해서 래쉬가드 수요가 증가함");
	private final GTPNewsDomain gtpNewsDomain = new GTPNewsDomain("배럴", "휴가로 인해서 래쉬가드 수요가 증가함", "수영복", null, null);

	@Test
	void ohlc에_등록된_뉴스가_없을때_get() {
		OHLC ohlc = new OHLC(1, 2, 3, 4, 5, null);
		assertTrue(ohlc.getNews().isEmpty());
	}

	@Test
	void ohlc에_여러_개의_뉴스가_등록되어_List형식으로_변환됨(){
		OHLC ohlc = new OHLC(1, 2, 3, 4, 5, null);
		gtpNewsDomain.addNewsDomain(news);

		ohlc.addRoseNews(gtpNewsDomain); //중복된 뉴스지만 저장됨
		ohlc.addRoseNews(gtpNewsDomain);

		assertEquals(2, ohlc.getNews().size());
	}
}
