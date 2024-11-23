package com.bjcareer.GPTService.application.port.in;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AnalyzeStockNewsCommand {
	public String title;
	public String content;
	public String stockName;
	public String newLink;
	public String imgLink;
	public String pubDate;

	public AnalyzeStockNewsCommand(String title, String content, String stockName, String newLink, String imgLink, String pubDate) {
		this.title = title;
		this.content = content;
		this.stockName = stockName;
		this.newLink = newLink;
		this.imgLink = imgLink;
		this.pubDate = pubDate;
	}
}
