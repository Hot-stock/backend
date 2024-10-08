package com.bjcareer.gateway.in.api.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor
public class StockAdditionRequestDTO {
	@NotBlank(message = "Stock name is mandatory")
	private String stockName;

	@NotBlank(message = "Thema is mandatory")
	private String thema;

	@NotBlank(message = "Code is mandatory")
	private String code;
}
