package com.bjcareer.GPTService.in.dtos;

import java.util.List;

import com.bjcareer.GPTService.domain.gpt.thema.GPTThema;

import lombok.Getter;

@Getter
public class ListThemaNewsResponseDTO {
	private final String keyword;
	private final List<ThemaResponseDTO> result;

	public ListThemaNewsResponseDTO(String keyword, List<GPTThema> themas) {
		this.keyword = keyword;
		result = themas.stream()
			.map(thema -> new ThemaResponseDTO(thema.getSummary(),
				new ThemaInfoResponseDTO(thema.getThemaInfo().getName(), thema.getThemaInfo().getReason()),
				thema.getUpcomingDate(), thema.getUpcomingDateReasonFact(), thema.getUpcomingDateReasonOpinion(),
				new NewsResponseDTO(thema.getNews()), thema.isPositive()))
			.toList();
	}
}
