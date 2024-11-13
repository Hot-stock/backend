package com.bjcareer.search.out.api.gpt.insight;

import java.time.LocalDate;
import java.util.List;

import software.amazon.awssdk.services.s3.endpoints.internal.Value;

class ShortTermStrategyVariableResponseDTO {
	// Key Date: 전략의 핵심 날짜
	public LocalDate date;

	// Catalyst Impact: 촉매 영향 분석
	public String catalystImpact;

	// Historical Impact: 유사한 과거 사건이 미친 영향
	public List<HistoricalImpactDetail> historicalImpact;

	// Predicted Impact: 과거 경향에 따른 예상 영향
	public String predictedImpact;

	// Probability of Increase: 예상 상승 확률
	public String probabilityOfIncrease;

	// Investor Insight: 슈퍼개미의 통찰력
	public String investorInsight;

	public static class HistoricalImpactDetail {

		// Impact Date: 과거 이벤트 발생 날짜
		public LocalDate impactDate;

		// Reason for Rise: 주가 상승 이유
		public String reasonForRise;
		// Source Link: 출처 링크
		public String sourceLink;
	}
}
