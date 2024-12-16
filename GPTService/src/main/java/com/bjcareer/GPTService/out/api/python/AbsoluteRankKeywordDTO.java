package com.bjcareer.GPTService.out.api.python;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AbsoluteRankKeywordDTO {
	private Long absoluteKeywordCount;
	private String period;

	public AbsoluteRankKeywordDTO(Double absoluteKeyword, String period) {
		this.absoluteKeywordCount = absoluteKeyword.longValue();
		this.period = period;
	}
}
