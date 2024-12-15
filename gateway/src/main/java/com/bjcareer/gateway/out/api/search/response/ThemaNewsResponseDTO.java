package com.bjcareer.gateway.out.api.search.response;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import lombok.Getter;

@Getter
public class ThemaNewsResponseDTO {
	private int total;
	private List<Content> items = new ArrayList<>();
	private List<String> themas = new ArrayList<>();

	@Data
	private static class Content {
		private String name;
		private String title;
		private String summary;
		private String imgLink;
		private String link;
		private String date;
	}
}
