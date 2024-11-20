package com.bjcareer.search.domain.gpt.thema;

import com.bjcareer.search.domain.News;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor
public class GPTThema {
	private boolean isRelatedThema;
	private String summary;
	private String upcomingDate;
	private String upcomingDateReason;
	private News news;

	public GPTThema(boolean isRelatedThema, String summary, String upcomingDate, String upcomingDateReason, String historyPattern, News news) {
		this.isRelatedThema = isRelatedThema;
		this.summary = summary;
		this.upcomingDate = upcomingDate;
		this.upcomingDateReason = upcomingDateReason;
		this.news = news;
	}
}
