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
	private String imgLink;
	private String description;
	private LocalDate pubDate;
	private String content;

	public News(String title, String newsLink, String imgLink, String description, String pubDate, String content) {
		this.title = title;
		this.originalLink = newsLink;
		this.imgLink = imgLink;
		this.description = description;
		this.pubDate = changeLocalDate(pubDate);
		this.content = content;
	}

	public News(String title, String newsLink, String imgLink, String description, LocalDate pubDate, String content) {
		this.title = title;
		this.originalLink = newsLink;
		this.imgLink = imgLink;
		this.description = description;
		this.pubDate = pubDate;
		this.content = content;
	}

	private LocalDate changeLocalDate(String pubDate) {

		if (pubDate.isEmpty()) {
			return null;
		}
		// 날짜 형식에 맞는 DateTimeFormatter 생성
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE, dd MMM yyyy HH:mm:ss VV");

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
			&& Objects.equals(imgLink, news.imgLink) && Objects.equals(description, news.description)
			&& Objects.equals(pubDate, news.pubDate) && Objects.equals(content, news.content);
	}

	@Override
	public int hashCode() {
		return Objects.hash(title, originalLink, imgLink, description, pubDate, content);
	}
}
