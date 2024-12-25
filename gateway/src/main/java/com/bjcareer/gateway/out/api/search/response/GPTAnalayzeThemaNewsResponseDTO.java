package com.bjcareer.gateway.out.api.search.response;

import lombok.Getter;

@Getter
public class GPTAnalayzeThemaNewsResponseDTO {
	private String themaName;
	private String summary;
	private String nextEventReason;
	private String nextDate;
	private NewsResponseDTO news;
}
