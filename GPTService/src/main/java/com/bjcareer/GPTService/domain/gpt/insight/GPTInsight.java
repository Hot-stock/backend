package com.bjcareer.GPTService.domain.gpt.insight;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@RequiredArgsConstructor
@ToString
public class GPTInsight {
	private final boolean isFound;
	private final String reason;
	private final String reasonDetail;
	private final Integer score;
}
