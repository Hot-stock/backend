package com.bjcareer.search.out.api.gpt;

import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.bjcareer.search.domain.GTPNewsDomain;

@SpringBootTest
class ChatGPTAdapterTest {
	@Autowired
	private ChatGPTAdapter ChatGPTAdapter;

	@Test
	void testParser() {
		Optional<GTPNewsDomain> stockRaiseReason = ChatGPTAdapter.findStockRaiseReason(
			"내 당뇨병 환자가 600만명을 넘어서며 혈당 관리가 개인의 건강을 유지하는 데 중요한 요소로 부각되고 있다.\n"
				+ "\n"
				+ "특히 한국인들의 경우 유전적으로 당뇨에 취약한 경향이 있어, 올바른 혈당 관리 솔루션의 필요성이 대두되고 있다.\n"
				+ "\n"
				+ "디지털 헬스케어 기업 마크로젠에 따르면 국가생명윤리정책원 가이드라인에 따라 마크로젠의 유전자검사 기반 건강관리 플랫폼 젠톡에서 선별한 혈당 관련 유전인자 12개를 자체 한국인 표준분포 데이터로 분석할 경우 약 6.2개를 보유하고 있는 것으로 나타났다. 이 유전인자들은 혈당을 조절하는 능력에 영향을 미쳐 혈당이 상승할 위험을 높일 수 있다.\n"
				+ "\n"
				+ "이에 제약·바이오 및 헬스케어 기업들은 혈당관리 플랫폼 또는 관련 캠페인 진행하며 혈당 관리 서비스 강화에 나서고 있다. 이를 통해 당뇨병 환자의 데이터를 확보하고, 더 나아가 당뇨병 치료제 개발 등에도 나설 수 있다는 장점이 있다.\n"
				+ "\n"
				+ "한독은 지난달 올바른 혈당관리법을 알리기 위해 '당당크루 1기' 활동을 진행했다.\n"
				+ "\n"
				+ "한독은 당뇨병 관리의 중요성과 심각한 합병증 당뇨발을 알리는 한편, 어려운 환경에서 당뇨병과 싸우고 있는 환우들을 지원해왔다. 올해는 젊은 당뇨환자가 급증하고 있는 사회적 문제를 함께 해결하기 위해 혈당관리가 필요한 젊은 당뇨인을 대상으로 당당크루 활동을 마련했다.\n"
				+ "\n"
				+ "33명의 당당크루들은 프로그램 시작 전 시시각각 변화하는 혈당 수치 확인을 위해 연속혈당측정기를 착용했다. 이후 식단 코치에게 혈당관리에 도움이 되는 식단관리법을 배우고 바나나와 쿠키 등 다양한 음식 중 원하는 것을 먹어본 뒤 음식에 따라 혈당의 변화를 직접 확인했다. 운동 코치에게 혈당관리에 도움이 되는 운동법에 대해서 배우는 시간도 가졌다.\n"
				+ "\n"
				+ "대웅제약은 관계사 엠서클이 최근 혈당관리 기반 헬스케어 플랫폼 '웰다'를 선보였다. 웰다는 웰 다이어트(Well Diet)라는 의미로, 혈당 관리를 기반으로 디지털 웨어러블 기기와 연동해 사용자의 혈당 수치, 식사, 운동량 등을 AI로 기록한 뒤 개인의 특성에 맞춘 1:1 건강 관리 솔루션을 제공하는 애플리케이션 서비스다.\n"
				+ "\n"
				+ "대웅제약의 경우 현재 보유하고 있는 당뇨병 신약 '엔블로'와 플랫폼 웰다와의 시너지 효과도 기대해볼 수 있다.\n"
				+ "\n"
				+ "이밖에 카카오헬스케어는 한국당원병환우회와 업무협약을 맺고 당원병 환우들의 질환 관리 등을 위해 AI, 디지털, 모바일 기술을 활용한 솔루션 개발에 협력하기로 했다. 카카오헬스케어는 올해 초 혈당 관리 플랫폼 파스타를 선보인 뒤 50여곳의 기관과 업무협약을 체결하고 데이터를 확보하고 있는데, 이번 협약을 통해 더욱 다양한 데이터를 확보할 수 있을 것으로 기대된다.\n",
			"대웅제약", LocalDate.of(2024, 11, 7));
		System.out.println("stockRaiseReason = " + stockRaiseReason.get());
	}

}
