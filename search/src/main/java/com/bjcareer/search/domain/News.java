package com.bjcareer.search.domain;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Objects;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@ToString
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class News {
	private String title;
	private String originalLink;
	private String link;
	private String description;
	private LocalDate pubDate;
	private String content;

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

	@Override
	public boolean equals(Object object) {
		if (this == object)
			return true;
		if (object == null || getClass() != object.getClass())
			return false;
		News news = (News)object;
		return Objects.equals(title, news.title) && Objects.equals(originalLink, news.originalLink)
			&& Objects.equals(link, news.link) && Objects.equals(description, news.description)
			&& Objects.equals(pubDate, news.pubDate) && Objects.equals(content, news.content);
	}

	@Override
	public int hashCode() {
		return Objects.hash(title, originalLink, link, description, pubDate, content);
	}
}
