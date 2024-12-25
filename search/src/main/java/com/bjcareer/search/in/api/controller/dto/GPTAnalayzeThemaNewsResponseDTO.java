package com.bjcareer.search.in.api.controller.dto;

import com.bjcareer.search.domain.gpt.thema.GPTThemaNewsDomain;

import lombok.Getter;

@Getter
public class GPTAnalayzeThemaNewsResponseDTO {
	private String themaName;
	private String summary;
	private String nextEventReason;
	private String nextDate;
	private NewsResponseDTO news;

	public GPTAnalayzeThemaNewsResponseDTO(GPTThemaNewsDomain gptThemaNewsDomain) {
		this.themaName = gptThemaNewsDomain.getName();
		this.summary = gptThemaNewsDomain.getSummary();
		this.nextEventReason = gptThemaNewsDomain.getUpcomingDateReason();
		this.nextDate = gptThemaNewsDomain.getUpcomingDate();
		this.news = new NewsResponseDTO(gptThemaNewsDomain.getNews());
	}
}
