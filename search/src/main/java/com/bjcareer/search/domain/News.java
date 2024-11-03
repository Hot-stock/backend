package com.bjcareer.search.domain;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import com.fasterxml.jackson.annotation.JsonFormat;

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
	private final String content;

	public News(String title, String originalLink, String link, String description, String pubDate, String content) {
		this.title = title;
		this.originalLink = originalLink;
		this.link = link;
		this.description = description;
		this.pubDate = changeLocalDate(pubDate);
		this.content = content;
	}
	private LocalDate changeLocalDate(String pubDate) {
		// 날짜 형식에 맞는 DateTimeFormatter 생성
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE, dd MMM yyyy HH:mm:ss VV", Locale.ENGLISH);

		// ZonedDateTime으로 파싱 (타임존 정보 포함)
		ZonedDateTime zonedDateTime = ZonedDateTime.parse(pubDate, formatter);

		// ZonedDateTime에서 LocalDate 추출
		return zonedDateTime.toLocalDate();
	}
}
