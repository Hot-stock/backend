package com.bjcareer.search.out.api.gpt.insight;

public class GPTResponseInsightFormatDTO {
	public String type = "json_schema";
	public JsonSchema json_schema = new JsonSchema();

	public static class JsonSchema {
		public String name = "stock_insight";
		public Schema schema = new Schema();
		public boolean strict = true;

		public static class Schema {
			public String type = "object";
			public Properties properties = new Properties();
			public String[] required = {"themeStrength", "buyRecommendation", "marketAnalysis", "requiredVolumeFor10PercentGain",
				"volumeDeclinePeriods", "investmentThesis"};
			public boolean additionalProperties = false;

			public static class Properties {
				public PropertyDetail themeStrength = new PropertyDetail("string",
					"Evaluate the clarity of the current stock theme and assess the likelihood of an increase based on repeated past patterns related to this theme."
				);

				public PropertyDetail buyRecommendation = new PropertyDetail("string",
					"Provide a recommendation on whether to buy this stock now, choosing one of the following options: 'Strong Buy,' 'Buy,' 'Hold,' or 'Do Not Buy.' Follow the chosen recommendation with immediate reasons for buying (or not), as well as any additional considerations or factors to watch."
				);

				public PropertyDetail marketAnalysis = new PropertyDetail("string",
					"Analyze the factors driving the stock’s rise, considering market conditions, competitor activity, and industry trends to explain the reason for the increase. Provide links to relevant news articles or reports supporting this analysis, if available."
				);

				public PropertyDetail requiredVolumeFor10PercentGain = new PropertyDetail("string",
					"Estimate the expected trading volume required for a 10% increase in the stock price, based on historical trading data."
				);

				public PropertyDetail volumeDeclinePeriods = new PropertyDetail("string",
					"Identify periods when trading volume decreased and the stock price dropped, analyzing key price levels during these periods to assess risk and potential support levels. Provide relevant links to support these observations, if available."
				);

				public PropertyDetail investmentThesis = new PropertyDetail("string",
					"Develop a rationale for investing based on key dates and events over the next 6 months. Include specific catalysts such as earnings reports, industry events, or policy announcements. Include links to any supporting news articles or official announcements, if available."
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
