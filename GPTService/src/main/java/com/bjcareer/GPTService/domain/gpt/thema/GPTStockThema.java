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
	private boolean isRelated;
	private String relatedDetail;
	private boolean isPositive;
	private List<ThemaInfo> themaInfo;
	@MongoId
	private String link;

	public GPTStockThema(boolean isRelated, boolean isPositive, String relatedDetail, OriginalNews news,
		List<ThemaInfo> themaInfo) {
		this.isRelated = isRelated;
		this.isPositive = isPositive;
		this.relatedDetail = relatedDetail;
		this.themaInfo = themaInfo;
		this.link = news.getNewsLink();
	}
}
