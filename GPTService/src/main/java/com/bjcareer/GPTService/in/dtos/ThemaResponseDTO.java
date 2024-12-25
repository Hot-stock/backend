package com.bjcareer.GPTService.in.dtos;

import java.time.LocalDate;

import lombok.Getter;

@Getter
public class ThemaResponseDTO {
	private final String summary;
	private final ThemaInfoResponseDTO themaInfo;
	private final LocalDate nextEventDate;
	private final String nextEventReasonFact;
	private final String nextEventReasonOpinion;
	private final NewsResponseDTO news;

	public ThemaResponseDTO(String summary, ThemaInfoResponseDTO themaInfo, LocalDate nextEventDate,
		String nextEventReasonFact, String nextEventReasonOpinion, NewsResponseDTO news) {
		this.summary = summary;
		this.themaInfo = themaInfo;
		this.nextEventDate = nextEventDate;
		this.nextEventReasonFact = nextEventReasonFact;
		this.nextEventReasonOpinion = nextEventReasonOpinion;
		this.news = news;
	}
}
