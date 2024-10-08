package com.bjcareer.search.out.api.dto;

import java.util.List;

import lombok.Data;

@Data
public class KeywordResponseDTO {
	private List<KeywordDto> keywordList;  // 키워드 리스트

	@Data
	public static class KeywordDto {
		private String relKeyword;            // 연관 키워드
		private int monthlyPcQcCnt;           // 월간 PC 검색량
		private int monthlyMobileQcCnt;       // 월간 모바일 검색량
		private double monthlyAvePcClkCnt;    // 월간 PC 평균 클릭 수
		private double monthlyAveMobileClkCnt;// 월간 모바일 평균 클릭 수
		private double monthlyAvePcCtr;       // 월간 PC 평균 클릭률
		private double monthlyAveMobileCtr;   // 월간 모바일 평균 클릭률
		private int plAvgDepth;               // 평균 페이지 뷰 깊이
		private String compIdx;               // 경쟁도 (중간, 높음, 낮음 등)
		private int totalQcCnt;               // 총 검색량

		public KeywordDto(String relKeyword, String monthlyPcQcCnt, String monthlyMobileQcCnt,
			double monthlyAvePcClkCnt,
			double monthlyAveMobileClkCnt, double monthlyAvePcCtr, double monthlyAveMobileCtr, int plAvgDepth,
			String compIdx) {
			this.relKeyword = relKeyword;
			this.monthlyPcQcCnt = convertStringToInt(monthlyPcQcCnt);
			this.monthlyMobileQcCnt = convertStringToInt(monthlyMobileQcCnt);
			this.monthlyAvePcClkCnt = monthlyAvePcClkCnt;
			this.monthlyAveMobileClkCnt = monthlyAveMobileClkCnt;
			this.monthlyAvePcCtr = monthlyAvePcCtr;
			this.monthlyAveMobileCtr = monthlyAveMobileCtr;
			this.plAvgDepth = plAvgDepth;
			this.compIdx = compIdx;
			this.totalQcCnt = this.monthlyMobileQcCnt + this.monthlyPcQcCnt;
		}

		private int convertStringToInt(String str) {
			try {
				return Integer.parseInt(str);
			} catch (NumberFormatException e) {
				return 0; // 변환이 불가능한 경우 0을 반환
			}
		}

	}
}
