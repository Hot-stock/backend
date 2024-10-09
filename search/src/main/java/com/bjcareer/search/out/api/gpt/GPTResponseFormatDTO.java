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
				public PropertyDetail name = new PropertyDetail("string", "주식 이름");
				public PropertyDetail reason = new PropertyDetail("string", "Summary of why the stock is rising (3 lines)");
				public PropertyDetail thema = new PropertyDetail("string", "기사에 나온 오른 이유를 요약한 테마");
				public PropertyDetail next = new PropertyDetail("string",
					"다음 테마가 시작하는 날짜를 오늘날짜기준으로 추출해줘 날짜형식은 YYYY-mm-dd로 고정 (날짜를 추출하지 못하면 null)");
				public PropertyDetail next_reason = new PropertyDetail("string",
					"왜 그렇게 됐는지 오늘날짜, 다음 테마 시작점, 그 이유 (이유가 없거나 이상한 문자열이 추출되면 null)");

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
