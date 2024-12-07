package com.bjcareer.search.domain.gpt.thema;

import com.bjcareer.search.domain.News;
import com.bjcareer.search.domain.entity.ThemaInfo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor
public class GPTThema {
	private String name;
	private String summary;
	private String upcomingDate;
	private String upcomingDateReason;
	private News news;

	public GPTThema(String themaName, String summary, String upcomingDate, String upcomingDateReason, News news) {
		this.name = themaName;
		this.summary = summary;
		this.upcomingDate = upcomingDate;
		this.upcomingDateReason = upcomingDateReason;
		this.news = news;
	}
}
