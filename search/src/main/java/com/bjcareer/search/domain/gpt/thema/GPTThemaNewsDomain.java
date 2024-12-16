package com.bjcareer.search.domain.gpt.thema;

import com.bjcareer.search.domain.News;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor
public class GPTThemaNewsDomain {
	private String name;
	private String summary;
	private String upcomingDate;
	private String upcomingDateReason;
	private News news;

	public GPTThemaNewsDomain(String themaName, String summary, String upcomingDate, String upcomingDateReason, News news) {
		this.name = themaName;
		this.summary = summary;
		this.upcomingDate = upcomingDate;
		this.upcomingDateReason = upcomingDateReason;
		this.news = news;
	}
}
