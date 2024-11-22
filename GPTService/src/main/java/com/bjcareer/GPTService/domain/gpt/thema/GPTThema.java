package com.bjcareer.GPTService.domain.gpt.thema;

import com.bjcareer.GPTService.domain.gpt.OriginalNews;

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
	private OriginalNews news;

	public GPTThema(boolean isRelatedThema, String summary, String upcomingDate, String upcomingDateReason,
		String historyPattern, OriginalNews news) {
		this.isRelatedThema = isRelatedThema;
		this.summary = summary;
		this.upcomingDate = upcomingDate;
		this.upcomingDateReason = upcomingDateReason;
		this.news = news;
	}
}
