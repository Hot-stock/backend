package com.bjcareer.search.domain;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Optional;

import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@ToString
public class News {
	private final String title;
	private final String originalLink;
	private final String link;
	private final String description;
	private final LocalDate pubDate;
	private Optional<String> content = Optional.empty();

	public News(String title, String originalLink, String link, String description, String pubDate) {
		this.title = title;
		this.originalLink = originalLink;
		this.link = link;
		this.description = description;
		this.pubDate = changeLocalDate(pubDate);
	}

	public void setContent(String content) {
		log.debug("뉴스 내용 : {}", content);
		this.content = Optional.of(content);
	}

	private LocalDate changeLocalDate(String pubDate) {
		// 날짜 형식에 맞는 DateTimeFormatter 생성
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE, dd MMM yyyy HH:mm:ss Z", Locale.ENGLISH);

		// ZonedDateTime으로 파싱 (타임존 정보 포함)
		ZonedDateTime zonedDateTime = ZonedDateTime.parse(pubDate, formatter);

		// ZonedDateTime에서 LocalDate 추출
		return zonedDateTime.toLocalDate();
	}
}
