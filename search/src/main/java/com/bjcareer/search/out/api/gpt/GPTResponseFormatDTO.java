package com.bjcareer.search.out.api.gpt;

public class GPTResponseFormatDTO {
	public String type = "json_schema";
	public JsonSchema json_schema = new JsonSchema();

	public static class JsonSchema {
		public String name = "stock_rise_reason";
		public Schema schema = new Schema();
		public boolean strict = true;

		public static class Schema {
			public String type = "object";
			public Properties properties = new Properties();
			public String[] required = {"name", "reason", "thema", "next", "next_reason"};
			public boolean additionalProperties = false;

			public static class Properties {
				public PropertyDetail name = new PropertyDetail("string",
					"Specify the stock name clearly. The stock name should only be used in the 'name' field and should not appear in the 'thema' field."
				);

				public PropertyDetail reason = new PropertyDetail("string",
					"Summarize the reason for the stock's increase in 3 lines. Emphasize the stock name in the explanation, and include any connection with specific events or individuals."
				);

				public PropertyDetail thema = new PropertyDetail("string",
					"Construct the theme of the stock by extracting key themes from the reason field. Prioritize keywords related to notable individuals (e.g., Donald Trump, Elon Musk) or major issues (e.g., election, space exploration), ensuring they are closely connected with the stock name if possible. The theme keywords should consist of one word each and, if multiple, separated by commas. The stock name itself should not be used as a theme. (max limit 3)"
				);
				public PropertyDetail next = new PropertyDetail("string",
					"Extract the expected date for the start of the next theme based on today's date in the format YYYY-mm-dd. If an exact date is not available but an estimated time frame can be inferred (e.g., 'in the next week', 'in the coming months'), approximate the date accordingly. If neither a precise nor estimated date can be determined, return an empty string."
						+ "답변은 한글로 ."
				);
				public PropertyDetail next_reason = new PropertyDetail("string",
					"Provide a detailed explanation of how the next theme's start date (next) was calculated. Include today's date, the inferred start date, and the specific reasoning or logic used for this estimation. Additionally, provide context on why the chosen date range (e.g., 'mid-January') is likely. This includes political timelines, scheduled government sessions, or expected industry reports that support the chosen timeframe. When referencing specific documents, reports, or announcements, include the relevant link(s) to substantiate the reasoning. For example, if the date was based on a policy announcement, add a link to the policy details. If no clear basis for the date exists, or if the data seems unusual, return null. No Fabricated Information: Ensure that all information provided in next_reason and other fields is directly supported by the source text or referenced materials. If the necessary details are absent, leave the field empty or return null as appropriate."
				);



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
}
