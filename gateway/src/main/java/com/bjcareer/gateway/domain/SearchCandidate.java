package com.bjcareer.gateway.domain;

import java.util.List;

import lombok.Getter;

@Getter
public class SearchCandidate {
	private Integer total;
	private List<Keyword> keywords;

	@Getter
	public static class Keyword {
		private String keyword;
	}
}
