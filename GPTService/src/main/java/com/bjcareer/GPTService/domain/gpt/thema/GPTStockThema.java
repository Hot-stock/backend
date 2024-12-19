package com.bjcareer.GPTService.domain.gpt.thema;

import java.util.List;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import com.bjcareer.GPTService.domain.gpt.OriginalNews;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@ToString
@NoArgsConstructor
@Document(collection = "stock-thema-news")
public class GPTStockThema {
	private boolean isPositive;
	private ThemaInfo themaInfo;
	@MongoId
	private String link;

	public GPTStockThema(String link, boolean isPositive, ThemaInfo themaInfo) {
		this.link = link;
		this.isPositive = isPositive;
		this.themaInfo = themaInfo;
	}
}
