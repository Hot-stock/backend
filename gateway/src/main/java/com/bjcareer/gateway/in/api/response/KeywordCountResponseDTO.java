package com.bjcareer.gateway.in.api.response;

import lombok.Getter;

@Getter
public class KeywordCountResponseDTO {
	private Long absoluteKeywordCount;
	private String period;

	public KeywordCountResponseDTO(Long absoluteKeywordCount, String period) {
		this.absoluteKeywordCount = absoluteKeywordCount;
		this.period = period;
	}
}
