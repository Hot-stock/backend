package com.bjcareer.GPTService.domain.gpt.thema;

import java.time.LocalDate;
import java.util.Objects;
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
	private boolean isPositive;
	private String summary;
	private LocalDate upcomingDate;
	private String upcomingDateReasonFact;
	private String upcomingDateReasonOpinion;
	private OriginalNews news;
	private ThemaInfo themaInfo;
	@MongoId
	private String link;

	public GPTThema(boolean isRelatedThema, boolean isPositive, String summary, String upcomingDate,
		String upcomingDateReasonFact, String upcomingDateReasonOpinion,
		OriginalNews news, ThemaInfo themaInfo) {
		this.isRelatedThema = isRelatedThema;
		this.isPositive = isPositive;
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

	public String createNextReason() {
		return String.format("%s 테마의 예정된 이벤트 일자는 %s고, 그 이유의 사실은 %s 의견은 %s\n", themaInfo.getName(), upcomingDate,
			upcomingDateReasonFact, upcomingDateReasonOpinion);
	}

	@Override
	public boolean equals(Object object) {
		if (this == object)
			return true;
		if (object == null || getClass() != object.getClass())
			return false;
		GPTThema gptThema = (GPTThema)object;
		return isRelatedThema == gptThema.isRelatedThema && isPositive == gptThema.isPositive && Objects.equals(
			summary, gptThema.summary) && Objects.equals(upcomingDate, gptThema.upcomingDate)
			&& Objects.equals(upcomingDateReasonFact, gptThema.upcomingDateReasonFact)
			&& Objects.equals(upcomingDateReasonOpinion, gptThema.upcomingDateReasonOpinion)
			&& Objects.equals(news, gptThema.news) && Objects.equals(themaInfo, gptThema.themaInfo)
			&& Objects.equals(link, gptThema.link);
	}

	@Override
	public int hashCode() {
		return Objects.hash(isRelatedThema, isPositive, summary, upcomingDate, upcomingDateReasonFact,
			upcomingDateReasonOpinion, news, themaInfo, link);
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
