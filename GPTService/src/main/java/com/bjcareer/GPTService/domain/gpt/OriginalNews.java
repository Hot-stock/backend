package com.bjcareer.GPTService.domain.gpt;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
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
		// 두 가지 날짜 패턴 정의
		String[] patterns = {
			"EEE, dd MMM yyyy HH:mm:ss VV", // 패턴 1
			"yyyy-MM-dd"                   // 패턴 2
		};

		// 패턴을 순회하며 입력값을 처리
		for (String pattern : patterns) {
			try {
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern, Locale.KOREA);

				if (pattern.contains("VV")) {
					// ZonedDateTime으로 파싱
					ZonedDateTime zonedDateTime = ZonedDateTime.parse(pubDate, formatter);
					return zonedDateTime.toLocalDate();
				} else {
					// LocalDate로 직접 파싱
					return LocalDate.parse(pubDate, formatter);
				}
			} catch (DateTimeParseException e) {
				// 현재 패턴으로 파싱 실패하면 다음 패턴으로 이동
			}
		}

		// 두 패턴 모두 실패한 경우 예외 던짐
		throw new IllegalArgumentException("지원하지 않는 날짜 형식입니다: " + pubDate);
	}
}
