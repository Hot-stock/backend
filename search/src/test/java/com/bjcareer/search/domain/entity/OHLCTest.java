package com.bjcareer.search.domain.entity;

import static org.junit.jupiter.api.Assertions.*;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import com.bjcareer.search.domain.News;
import com.bjcareer.search.domain.gpt.GPTNewsDomain;

@ExtendWith(MockitoExtension.class)
class OHLCTest {

	private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE, dd MMM yyyy HH:mm:ss VV",
		Locale.ENGLISH);
	private final String pubDate = ZonedDateTime.now().format(formatter);
	Map<String, String> themas = Map.of("수영복", "휴가로 인해서 래쉬가드 수요가 증가함");
	private final News news = new News("title", "link", "link", "묘사", pubDate, "휴가로 인해서 래쉬가드 수요가 증가함");
	private final GPTNewsDomain GPTNewsDomain = new GPTNewsDomain("배럴", "휴가로 인해서 래쉬가드 수요가 증가함", new ArrayList<>(), null, null);

	@Test
	void ohlc에_등록된_뉴스가_없을때_get() {
		OHLC ohlc = new OHLC(1, 2, 3, 4, 5, 10L,null);
		assertTrue(ohlc.getNews().isEmpty());
	}

	@Test
	void ohlc에_여러_개의_뉴스가_등록되어_List형식으로_변환됨(){
		OHLC ohlc = new OHLC(1, 2, 3, 4, 5,10L, null);
		GPTNewsDomain.addNewsDomain(news);

		ohlc.addRoseNews(GPTNewsDomain); //중복된 뉴스지만 저장됨
		ohlc.addRoseNews(GPTNewsDomain);

		assertEquals(2, ohlc.getNews().size());
	}
}
