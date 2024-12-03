package com.bjcareer.gateway.out.api.search.response;

import java.util.List;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class StockerFilterResultResponseDTO {
	private String keyword;
	private List<Content> itmes;

	@Getter
	static class Content {
		private String name;
		private String code;
		private List<String> themes;

	}
}
