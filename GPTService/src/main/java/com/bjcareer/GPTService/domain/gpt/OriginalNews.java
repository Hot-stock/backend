package com.bjcareer.GPTService.domain.gpt;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@ToString
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class OriginalNews {
	private String title;
	private String newsLink;
	private String imgLink;
	private String content;
	private LocalDate pubDate;

	public OriginalNews(String title, String newsLink, String imgLink, String pubDate, String content) {
		this.title = title;
		this.newsLink = newsLink;
		this.imgLink = imgLink;
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
