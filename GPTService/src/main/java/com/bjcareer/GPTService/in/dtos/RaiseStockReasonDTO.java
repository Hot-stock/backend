package com.bjcareer.GPTService.in.dtos;

import java.time.LocalDate;

import com.bjcareer.GPTService.domain.gpt.GPTNewsDomain;

import lombok.Getter;

@Getter
public class RaiseStockReasonDTO {
	private NewsResponseDTO news;
	private String summary;
	private LocalDate next;
	private String nextReasonFact;
	private String nextReasonOpinion;

	public RaiseStockReasonDTO(NewsResponseDTO news, GPTNewsDomain newsDomain) {
		this.news = news;
		this.summary = newsDomain.getReason();
		this.next = newsDomain.getNext().orElseGet(() -> null);
		this.nextReasonFact = newsDomain.getNextReasonFact();
		this.nextReasonOpinion = newsDomain.getNextReasonOption();
	}
}
