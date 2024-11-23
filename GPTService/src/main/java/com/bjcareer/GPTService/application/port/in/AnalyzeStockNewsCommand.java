package com.bjcareer.GPTService.application.port.in;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AnalyzeStockNewsCommand {
	public String newsLink;

	public AnalyzeStockNewsCommand(String newsLink) {
		this.newsLink = newsLink;
	}
}
