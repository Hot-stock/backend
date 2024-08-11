package com.bjcareer.search.controller.dto;

import java.util.ArrayList;
import java.util.List;

import com.bjcareer.search.domain.entity.Suggestion;

import lombok.Data;
import lombok.Getter;

@Getter
public class QueryResponseDTO {
	private int total;
	private List<Keyword> keywords = new ArrayList<>();

	public QueryResponseDTO(List<Suggestion> rankings) {
		this.total = rankings.size();
		rankings.forEach(r -> keywords.add(new Keyword(r.getKeyword())));
	}

	@Data
	public static class Keyword{
		private final String keyword;
	}
}
