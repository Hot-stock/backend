package com.bjcareer.search.out.api.dto;

import java.util.List;

import lombok.Data;

@Data
public class NewsResponseDTO {
	private String lastBuildDate;
	private int total;
	private int start;
	private int display;
	private List<NewsItemDTO> items;
}
