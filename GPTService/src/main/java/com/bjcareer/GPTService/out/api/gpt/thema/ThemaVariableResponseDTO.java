package com.bjcareer.GPTService.out.api.gpt.thema;

import java.util.List;

import lombok.Data;

@Data
public class ThemaVariableResponseDTO {
	private List<String> stockNames;
	private String name;
	private String reason;
}
