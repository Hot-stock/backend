package com.bjcareer.gateway.out.api.search.response;

import java.util.List;

import lombok.Data;
import lombok.Getter;

@Getter
public class ThemaNamesResponseDTO {
	private Integer total;
	private List<Content> contents;

	@Data
	private static class Content {
		private Long id;
		private String thema;
	}
}
