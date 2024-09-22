package com.bjcareer.search.out.api.dto;

import java.util.List;

import lombok.Data;

@Data
public class DataLabTrendRequestDTO {
	public String startDate;
	public String endDate;
	public String timeUnit;
	public List<KeywordGroup> keywordGroups;

	public DataLabTrendRequestDTO(String startDate, String endDate, List<KeywordGroup> keywordGroups) {
		this.startDate = startDate;
		this.endDate = endDate;
		this.timeUnit = "date";
		this.keywordGroups = keywordGroups;
	}

	@Data
	public static class KeywordGroup {
		public String groupName;
		public List<String> keywords;

		public KeywordGroup(String groupName, List<String> keywords) {
			this.groupName = groupName;
			this.keywords = keywords;
		}
	}
}

