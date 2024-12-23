package com.bjcareer.GPTService.domain.gpt.thema;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import com.bjcareer.GPTService.domain.gpt.OriginalNews;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@ToString
@NoArgsConstructor
@Document(collection = "thema-news")  // MongoDB의 "news" 컬렉션에 매핑
public class GPTThema {
	private boolean isRelatedThema;
	private String summary;
	private LocalDate upcomingDate;
	private String upcomingDateReasonFact;
	private String upcomingDateReasonOpinion;
	private ThemaInfo themaInfo;
	private OriginalNews news;
	@MongoId
	private String link;

	public GPTThema(boolean isRelatedThema, String summary, String upcomingDate,
		String upcomingDateReasonFact, String upcomingDateReasonOpinion,
		OriginalNews news, ThemaInfo themaInfo) {
		this.isRelatedThema = isRelatedThema;
		this.summary = summary;
		this.upcomingDate = parseLocalDate(upcomingDate);
		this.upcomingDateReasonFact = upcomingDateReasonFact;
		this.upcomingDateReasonOpinion = upcomingDateReasonOpinion;
		this.news = news;
		this.themaInfo = themaInfo;
		this.link = news.getNewsLink();
	}

	public Optional<LocalDate> getNext() {
		return Optional.ofNullable(upcomingDate);
	}

	private LocalDate parseLocalDate(String next) {
		if (next == null || next.isEmpty()) {
			return null;
		} else {
			try {
				return LocalDate.parse(next);
			} catch (Exception e) {
				log.error("Failed to parse date: {}", next);
				return null;
			}
		}
	}
}
