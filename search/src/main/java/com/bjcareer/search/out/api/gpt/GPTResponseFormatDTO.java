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
			public String[] required = {"name", "reason", "thema", "next"};
			public boolean additionalProperties = false;

			public static class Properties {
				public PropertyDetail name = new PropertyDetail("string", "The stock name");
				public PropertyDetail reason = new PropertyDetail("string", "Summary of why the stock is rising (3 lines)");
				public PropertyDetail thema = new PropertyDetail("string", "The thematic category related to the stock");
				public PropertyDetail next = new PropertyDetail("string", "뉴스 발행일 기준으로 다음 이벤트 발생할 수 있는 시점을 알려줘 (null if not applicable)");

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
