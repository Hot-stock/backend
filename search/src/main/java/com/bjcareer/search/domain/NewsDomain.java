package com.bjcareer.search.domain;

import java.io.IOException;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Optional;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
public class NewsDomain {
	public static final String META_NAME_DESCRIPTION = "meta[name=description]";
	public static final String ATTRIBUTE_KEY = "content";

	private final String title;
	private final String originalLink;
	private final String link;
	private final String description;
	private final LocalDate pubDate;

	public NewsDomain(String title, String originalLink, String link, String description, String pubDate) {
		this.title = title;
		this.originalLink = originalLink;
		this.link = link;
		this.description = description;
		this.pubDate = changeLocalDate(pubDate);
	}

	/**
	 * 기사 내용을 추출하는 메서드
	 *
	 * @return 추출된 내용 또는 기본 설명 (meta description) 반환
	 */
	public Optional<String> getContent() {
		Document document = fetchDocumentFromLink();

		if (document == null) {
			return Optional.empty();
		}

		// 2. 메타 설명과 본문 텍스트 가져오기
		String metaDescription = extractMetaDescription(document);
		String content = extractBodyContent(document);

		// 3. 메타 설명이 없으면 기본 메시지 반환
		if (metaDescription == null) {
			log.warn("메타 설명이 없습니다: {}", this.link);
			return Optional.empty();
		}

		// 4. 공백 제거 및 인덱스 확인 후 내용 추출
		return Optional.ofNullable(findContentStartingFromMeta(content, metaDescription));
	}

	// 링크로부터 Document 객체를 가져오는 메서드
	private Document fetchDocumentFromLink() {
		try {
			return Jsoup.connect(this.link).get();
		} catch (IOException e) {
			log.error("링크에서 내용을 가져오는 중 오류가 발생했습니다: {}", this.link, e);
			return null;
		}
	}

	// Document 객체에서 메타 설명을 추출하는 메서드
	private String extractMetaDescription(Document document) {
		Element element = document.selectFirst(META_NAME_DESCRIPTION);
		return (element != null) ? element.attr(ATTRIBUTE_KEY) : null;
	}

	// Document 객체에서 본문 내용을 추출하는 메서드
	private String extractBodyContent(Document document) {
		return document.body().text();
	}

	// 본문에서 메타 설명 이후의 내용을 찾아 반환하는 메서드
	private String findContentStartingFromMeta(String content, String metaDescription) {
		// 공백 제거 (모든 공백 문자 제거)
		String cleanedContent = content.replaceAll(" ", "");
		String cleanedMeta = metaDescription.replaceAll(" ", "");

		// 메타 설명이 본문에 포함된 인덱스 찾기
		int targetIndex = cleanedContent.indexOf(cleanedMeta);

		if (targetIndex != -1) {
			// 메타 설명 이후의 본문 텍스트 반환
			return cleanedContent.substring(targetIndex);
		} else {
			return null;
		}
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
