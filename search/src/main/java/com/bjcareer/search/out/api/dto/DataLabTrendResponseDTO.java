package com.bjcareer.search.out.api.dto;

import java.util.List;

import lombok.Data;

@Data
public class DataLabTrendResponseDTO {
	public String startDate;   // 조회 기간 시작 날짜(yyyy-mm-dd 형식)
	public String endDate;     // 조회 기간 종료 날짜(yyyy-mm-dd 형식)
	public String timeUnit;    // 구간 단위
	public List<Result> results; // 결과 목록

	// Result 내부 클래스
	@Data
	public static class Result {
		public String title;   // 주제어
		public List<String> keywords; // 주제어에 해당하는 검색어 목록
		public List<Info> data; // 구간별 데이터 목록

		// Data 내부 클래스
		@Data
		public static class Info {
			public String period; // 구간별 시작 날짜(yyyy-mm-dd 형식)
			public String ratio;  // 구간별 검색량의 상대적 비율
		}
	}
}

