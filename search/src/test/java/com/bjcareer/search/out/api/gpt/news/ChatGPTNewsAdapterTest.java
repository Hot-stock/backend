package com.bjcareer.search.out.api.gpt.news;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.bjcareer.search.domain.gpt.GTPNewsDomain;

@SpringBootTest
class ChatGPTNewsAdapterTest {
	@Autowired
	private ChatGPTNewsAdapter ChatGPTNewsAdapter;

	@Test
	void 비정상적인_뉴스임으로_isFiltered는_true가_됨() {
		Optional<GTPNewsDomain> stockRaiseReason = ChatGPTNewsAdapter.findStockRaiseReason(
			"진성티이씨는 11월 8일 11시 18분 전일 대비 약 11% 급등한 8,050원에 거래되고 있다. 진성티이씨는 코스닥 상장 기업으로 기계,장비 업종에 속해 있다. 시가총액은 1,629억원으로 코스닥 상장기업 중 309위에 위치 해 있다.\n"
			+ "\n"
				+ "[종목 성향 진단]\n"
			+ "\n"
				+ "진성티이씨 11 급등 전일 보다 800원 상승한 8050원\n"
				+ "[그림 2] 종목 성향 진단\n"
			+ "\n"
			+ "\n"
				+ "\u200B\n"
				+ "진성티이씨의 투자 스타일은 모호한 측면이 있으나 가치주라고 볼 수 있다. 기업 가치 대비 낮은 주가가 매력적인 기업이다.\n"
				+ "주가 모멘텀은 좋지 못했다. 최근 6개월 수익률은 -35.84%의 하락폭을 기록했다. 최근 1개월 수익률 또한 -25.1% 하락하며 부진한 모멘텀을 이어가고 있다.\n"
				+ "진성티이씨 종목에 대한 투자자의 관심도는 높은 편에 속한다.\n"
			+ "\n"
				+ "[투자 점수 진단]\n"
			+ "\n"
				+ "진성티이씨 11 급등 전일 보다 800원 상승한 8050원\n"
				+ "[그림 3] 투자 점수 진단\n"
			+ "\n"
			+ "\n"
				+ "진성티이씨는 어느 측면에서 투자 매력도가 높은 종목일까?\n"
			+ "\n"
			+ "\n"
				+ "AI 인공지능 종목 분석 시스템을 이용해 성장성, 수익성, 효율성, 안전성, 저평가성, 추세 등 주가 수익률에 영향을 줄 수 있는 6가지 핵심 투자 지표를 점수화하여 종목의 투자 매력도를 계산해보았다.\n"
				+ "그 결과 진성티이씨는 상대적으로 수익성 측면에서 두각을 드러내고 있었다.\n"
			+ "\n"
				+ "수익성 점수는 기업이 가진 자산과 자본을 활용하여 얼마나 이익을 실현했는지를 나타낸다.\n"
			+ "\n"
				+ "진성티이씨는 특히 ROE 측면에서 가장 높은 점수는 기록했는데, 전체 시장 내 267위를 기록하며 상위 13.37%에 위치했다. 업종별로는 34위를 기록하며 상위 9.68%에 위치했다.\n"
				+ "\n"
				+ "그러나 주가 상승 모멘텀을 나타내는 추세 측면에서는 아쉬움을 나타냈다.\n"
			+ "\n"
				+ "(위의 AI인공지능 점수는 재무 데이터를 기반으로 전체 상장 종목과 비교/분석하여 도출한 점수로 높은 점수가 반드시 높은 수익률을 보장하지 않습니다. 위의 자료는 당사의 추천 종목이 아니며, 투자 시 참고용으로 제시해드리는 것입니다. 핵심투자지표를 결합해 종목의 투자매력도를 종합 계산한 \"인공지능 투자점수\"는 \"거장들의 투자공식\" 앱에서 확인하실 수 있습니다.)",
			"진성티이씨", "", LocalDate.of(2024, 11, 8));
		assertTrue(stockRaiseReason.isEmpty());
	}

	@Test
	void 정상적인_뉴스임으로_isFiltered는_false가_됨() {
		Optional<GTPNewsDomain> stockRaiseReason = ChatGPTNewsAdapter.findStockRaiseReason(
			"진양산업 주가가 상승 중이다.\n"
				+ "\n"
				+ "14일 한국거래소에 따르면 진양산업은 이날 오전 10시 11분 기준 전 거래일 대비 230원 (2.55%) 오른 9240원에 거래 중이다.\n"
				+ "\n"
				+ "진양산업은 오세훈 서울시장 관련주다.\n"
				+ "\n"
				+ "지난 13일 6·1 지방선거에서 국민의힘 서울시장 후보로 나서는 오세훈 현 서울시장이 더불어민주당 예비 후보들과의 양자 가상대결에서 앞선다는 여론조사 결과가 나왔다.\n"
				+ "\n"
				+ "관련기사\n"
				+ "백종원 더본코리아 사흘 만에 주가 10% 하락…연기금도 물렸나\n"
				+ "'액티브 명가' 타임폴리오, 밸류업 ETF 성과 1위…\"주가 모멘텀도 고려\"\n"
				+ "리얼미터가 MBN 의뢰로 지난 11∼12일 서울시 거주 만 18세 이상 남녀 802명을 상대로 오 시장과 민주당 송영길 전 대표 중 누구에게 투표하겠냐고 물은 결과, 오 시장은 50.8%, 송 전 대표는 39.0%를 기록했다.\n"
				+ "\n"
				+ "다음 선거 발표는 다음주쯤으로 기약된다.\n"
				+ "지지율 격차는 11.8%포인트로 오차범위(95% 신뢰수준에서 ±3.5%포인트) 밖이었다.\n"
				+ "\n"
				+ "자세한 내용은 중앙선거여론조사심의위원회 홈페이지를 참조하면 된다.\n", "진양산업", "", LocalDate.of(2022, 4, 14));

		System.out.println("stockRaiseReason.get() = " + stockRaiseReason.get());
	}

}
