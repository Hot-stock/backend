package com.bjcareer.search.out.api.gpt.news;

public class GPTResponseNewsFormatDTO {
	public String type = "json_schema";
	public JsonSchema json_schema = new JsonSchema();

	public static class JsonSchema {
		public String name = "stock_rise_reason";
		public Schema schema = new Schema();
		public boolean strict = true;

		public static class Schema {
			public String type = "object";
			public Properties properties = new Properties();
			public String[] required = {"isFiltered", "name", "reason", "thema", "next", "next_reason"};
			public boolean additionalProperties = false;

			public static class Properties {
				public PropertyDetail isFiltered = new PropertyDetail("boolean",
					"When analyzing the stock's rise, set this to true if there is no specific explanation. For example, if the news only says the stock is 'undervalued' or has 'high investor interest' without a clear reason like a policy change or a company announcement, set this to true. Additionally, filter news with general terms like 'positive sentiment', 'growth expectations', 'sector performance', or 'profitability score' if no concrete event is mentioned. Set to false if a clear reason, like a major event or revenue increase for a related company, is provided."
				);

				public PropertyDetail name = new PropertyDetail("string",
					"The stock name should be specified here clearly and only used in this field, not in the 'thema' field."
				);

				public PropertyDetail reason = new PropertyDetail("string",
					"Summarize the reason for the stock’s increase in 3 lines, highlighting the stock name and any connection to specific events or people."
				);

				public PropertyDetail thema = new PropertyDetail("string",
					"Construct the stock’s theme from keywords in the reason field. Prioritize themes involving notable figures (e.g., 'Trump', 'Musk') or major issues (e.g., 'election', 'space'), using keywords of one word each, separated by commas. Avoid using the stock name itself as a theme. (max 3 keywords)"
				);

				public PropertyDetail next = new PropertyDetail("string",
					"Pick the closest significant date related to the topic, formatted as YYYY-mm-dd, based on today’s date. The date should be closely relevant to the theme (e.g., November 14 for World Diabetes Day if related to diabetes). Use dates with strong relevance and at least 80% confidence. If no reliable date is found, leave this field empty. Answer in Korean."
				);

				public PropertyDetail next_reason = new PropertyDetail("string",
					"Explain why the chosen date in `next` was selected, detailing any associated event or campaign and its relevance to the theme. For instance, if November 14 is picked due to World Diabetes Day, describe its connection to the topic and expected impact. Briefly mention longer-term events if they add context, but base `next` only on closely relevant events. If no strong reason for the date is available, return null."
				);
			}

			public static class PropertyDetail {
				public String type;
				public String description;

				public PropertyDetail(String type, String description) {
					this.type = type;
					this.description = description;
				}
			}
		}
	}
}
