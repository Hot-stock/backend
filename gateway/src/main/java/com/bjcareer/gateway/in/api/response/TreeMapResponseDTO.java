package com.bjcareer.gateway.in.api.response;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;

@Getter
public class TreeMapResponseDTO {
	private String name;
	private Double value;
	private final List<Content> children = new ArrayList<>();

	@Getter
	private static class Content {
		private final String name;
		private final String value;

		public Content(String name, String value) {
			this.name = name;
			this.value = value;
		}
	}
}
