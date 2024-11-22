package com.bjcareer.GPTService.application;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class SaveGPTStockNewsCommand {
	public String title;
	public String content;
	public String stockName;
	public String link;
	public String pubDate;

	public SaveGPTStockNewsCommand(String title, String content, String stockName, String link, String pubDate) {
		this.title = title;
		this.content = content;
		this.stockName = stockName;
		this.link = link;
		this.pubDate = pubDate;
	}
}
