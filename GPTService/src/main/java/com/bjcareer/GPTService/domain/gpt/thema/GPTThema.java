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
	private ThemaInfo themaInfo;

	public GPTThema(boolean isRelatedThema, String summary, String upcomingDate, String upcomingDateReason,
		OriginalNews news, ThemaInfo themaInfoes) {
		this.isRelatedThema = isRelatedThema;
		this.summary = summary;
		this.upcomingDate = upcomingDate;
		this.upcomingDateReason = upcomingDateReason;
		this.news = news;
		this.themaInfo = themaInfoes;
	}
}
