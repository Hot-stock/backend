package com.bjcareer.GPTService.out.api.gpt.insight.score.prompt;

import java.time.LocalDate;

public class QuestionPrompt {
	public static String QUESTION_FORMAT =
		"오늘의 날짜는 " + LocalDate.now() + "입니다.\n\n"

			+ "### 주식 정보 ###\n"
			+ "- 주식 이름: <stockName>%s</stockName>\n"
			+ "- 주식 상승 이유: <history>%s</history>\n"
			+ "- 테마 정보: <themaInfo>%s</themaInfo>\n"
			+ "- 관련 뉴스: <relatedNews>%s</relatedNews>\n"
			+ "- <relatedNews></relatedNews> XML에 배열 데이터는 총 %s 데이터가 있습니다.\n"

			+ "Imagine three different experts are answering this question. "
			+ "All experts act like stock analysts, writing down one step of their thinking, "
			+ "then sharing it with the group. "
			+ "Next, all experts move on to the following step, and so on. "
			+ "If any expert realises they're wrong at any point, they leave."

			+ "### 가이드라인: 데이터 추출 및 분석 ###\n"

			+ "**반드시 뉴스에서 명시적으로 언급된 사실만을 사용해야 합니다.**\n"

			+ "#### 1. 데이터 추출 지침 ####\n"
			+ "1.1 뉴스 데이터:\n"
			+ "   - <relatedNews></relatedNews> XML 태그 내부의 JSON 데이터를 역직렬화하여 다음 필드를 추출:\n"
			+ "     - news: 뉴스 내용\n"
			+ "     - pubDate: 뉴스 발행 날짜 (ISO 8601 형식 권장)\n"
			+ "     - nextEvent: 예정된 이벤트\n"
			+ "   - 역직렬화된 데이터의 총 개수와 작업 방법을 설명.\n"
			+ " 이 추출된 객체들은 NewsDTO라고 명명합니다."

			+ "1.2 테마 정보:\n"
			+ "   - <themaInfo></themaInfo> JSON 데이터를 역직렬화하여 다음 필드를 추출:\n"
			+ "     - thema: 테마 이름\n"
			+ "     - background: 테마 생성 배경 정보\n"
			+ "     - trigger: 해당 테마의 트리거 요소\n"
			+ "   - 총 몇 개의 테마 데이터가 추출되었는지, 추출 방법을 기술.\n"
			+ " 이 추출된 객체들은 themaInfoDTO라고 명명합니다."

			+ "1.3 주식 상승 이유:\n"
			+ "   - <history></history>에 기록된 주식 상승 이유를 ','로 구분하여 리스트로 추출.\n"
			+ "   - 각 이유가 현재 주식 상승에 기여했는지 여부를 분석.\n"

			+ "#### 2. 이벤트 추출 ####\n"
			+ "2 예정된 이벤트 추출:\n"
			+ " - NewsDTO.eventData를 참고하여 현재 날짜보다 이후에 발생할 예정인 이벤트들을 모두 추출합합니다. \n"
			+ "   - 추출된 이벤트들을 다음과 같은 형식으로 이벤트를 정리:\n"
			+ "     | 이벤트 이름 | 날짜 | 세부 설명 |\n"
			+ "   - 이벤트와 history, background간의 연결성이 있는지 생각하고 있다면 왜 연결성을 가지는지 NewsDTO.news를 근거로 파악합니다.\n"

			+ "2.1 이벤트 이름 추출 방법:\n"
			+ "- 모든 `nextEvent` 필드에서 이벤트 이름과 내용을 다음 단계를 통해 추출합니다:\n"

			+ "  1. 텍스트를 분석하여 주요 키워드와 문장을 파악합니다.\n"
			+ "     - 예: '양회에서 지급준비율 인하가 논의될 예정이다.'\n"

			+ "  2. 주어와 목적어를 기반으로 이벤트 이름과 세부 내용을 나눕니다.\n"
			+ "     - 예:\n"
			+ "	 - 추출된 문장: '내년 3월 양회에서 재정적자율 인상\n"
			+ "     - 이벤트 이름: '양회'(주어)\n"
			+ "     - 이벤트 내용: '지급준비율 인하 논의'(목적어)\n"

			+ "  3. 날짜 정보를 파악하여 이벤트와 연결합니다.\n"
			+ "     - 날짜: '2025년 초'\n"

			+ "  4. 최종 형식으로 정리:\n"
			+ "     | 이벤트 이름  | 날짜      | 이벤트 내용  \n"
			+ "     | 영회 인하   | 2025년 초 | 재정적자율 인상 기대 |\n"
			+ " 왜 이 이벤트가 추출 됐는지 사용자에게 설명해야 합니다.\n"

			+ "#### 3. 과거 데이터와의 연결 ####\n"
			+ "3.1 이벤트와 history 분석:\n"
			+ "   - 각 이벤트가 history 연결되는지 평가.\n"
			+ "   - 예:\n"
			+ "     - '2024년 12월 30일 저출산 정책 강화 발표' -> 2023년 유사한 정책 발표로 유아용품 주가 상승.\n"

			+ "3.2 이벤트와 background 분석:\n"
			+ "   - 각 이벤트가 themaInfo.background 연결되는지 분석.\n"
			+ "   - 분석 결과를 테이블로 정리:\n"
			+ "     | 이벤트 이름 | 트리거 | 배경 정보 |\n"

			+ "3.2 이벤트와 trigger 분석:\n"
			+ "   - 각 이벤트가 themaInfo.trigger와 연결되는지 분석.\n"
			+ "   - 분석 결과를 테이블로 정리:\n"
			+ "     | 이벤트 이름 | 트리거 | 배경 정보 |\n"
			+ "- 과거 데이터를 기반으로 현재 이벤트의 잠재적 영향을 예측.\n"

			+ "#### 4. 결론 분석 ####\n"
			+ "history, background, trigger와 연관된 이벤트 데이터들만을 추출합니다.\n"
			+ "연관된 이벤트 데이터들만을 사용하여 미래의 일어날 일을 사실을 바탕으로 예견하고 과거 데이터를 바탕으로 결론과 주장의 문장을 구사해야 합니다.\n"

			+ "#### 4. 최종 출력 ####\n"
			+ "답변은 한글로 작성하세요\n"
			+ "추출된 인사이트에는 몇 개의 뉴스를 분석했다는 기사를 포함해야 하고, 이벤트 이름과 이벤트의 내용을 명시적으로 언급하고 실행될 날짜가 언제인지와 명시적으로 언급해야 하며 날짜의 경우 최대한 정확하게 말해야합니다.\n"
			+ "날짜데이터는 최대한 구체적일 수록 좋다 예를 들어 '2025년 3월 1일'이 '2025년'보다 좋다.\n"
			+ "미래의 일어날 일을 바탕으로 과거 데이터를 분석한 후 매매에 적용하고, 결론과 주장으로 문장의 끝맺음을 구사해야 합니다.\n";
}
