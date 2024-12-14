package com.bjcareer.search.in.api.controller.dto;

import java.util.List;

import lombok.Getter;

@Getter
public class ThemaInfoResponseDTO {
	private List<String> stockName;
	private String name;
	private String reason;
}
