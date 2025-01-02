package com.bjcareer.GPTService.out.api.gpt.thema.stockNews.dtos;

import com.bjcareer.GPTService.out.api.gpt.JsonSchema;
import com.bjcareer.GPTService.out.api.gpt.Schema;
import com.bjcareer.GPTService.out.api.gpt.thema.stockNews.StockNewsThemaProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

public class GPTResponseStockNewsOfThemaFormatDTO {
	public String type = "json_schema";

	@JsonProperty("json_schema")
	public JsonSchema jsonSchema = new JsonSchema("thema_news_analysis",
		new Schema(StockNewsThemaProperties.required, new StockNewsThemaProperties()), true);
}
