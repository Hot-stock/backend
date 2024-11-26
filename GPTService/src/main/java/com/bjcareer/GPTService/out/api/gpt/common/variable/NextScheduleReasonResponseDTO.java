package com.bjcareer.GPTService.out.api.gpt.common.variable;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class NextScheduleReasonResponseDTO {
	private String fact;
	private String opinion;

	public NextScheduleReasonResponseDTO(String fact, String opinion) {
		this.fact = fact;
		this.opinion = opinion;
	}
}
