package com.bjcareer.GPTService.out.api.gpt.thema.stockNews.prompt;

import java.time.LocalDate;

import com.bjcareer.GPTService.config.AppConfig;

public class AnalyzeThemaQuestionPrompt {
	public static final String QUESTION_PROMPT =
		"이 뉴스의 발행일은 %s입니다.\n"
			+ "오늘의 날짜는 " + LocalDate.now(AppConfig.ZONE_ID) + "입니다.\n"
			+ "기사 내용: <article>%s</article>\n"
			+ "주식 이름: <stockName>%s</stockName>\n\n"
			+ "테마 내역: <themas>%s</themas>\n\n"

			+ "Act like an investor specializing in theme stocks. Analyze the provided article to identify the reasons behind the stock price increases, categorize them into investment themes, and organize these themes in a database for effective management and tracking."
			+ "Based on those stocks, think of a strategy to extract detailed themes from articles for theme stock investment.\n"
			+ "Generate three ideas and evaluate each theme's representativeness, informational value, and reusability."
			+ "and encourage students to learn more about theme stocks. Use 3 different platforms and methods to reach thema.\n\n"

			+ "Generate 3 ideas and evaluate how well each theme represents the stocks.\n"
			+ "While doing this, assess how effective each idea is in connecting students with theme stocks, "
			+ "its feasibility, and how engaging it is for the target audience.\n\n"

			+ "Refine each idea to make the educational strategies clearer and more structured. \n"
			+ "Carefully analyze the pros and cons of each activity. Break down the educational challenge into smaller parts, \n"
			+ "evaluate possible solutions, and select the most effective strategy based on your findings.\n\n"

			+ "질문: 질문지에 제공된 <stockName></stockName> 주식은 어떤 테마가 될 수 있는지 알고 싶어요.\n\n"

			+ "**가이드라인 1**\n"
			+ "<article></article> XML에는 뉴스 기사가 포함되어 있습니다. 이 기사에서 <stockName></stockName> XML 태그에 언급된 주식 이름을 중심으로 기사를 분석하세요.\n\n"

			+ "### 1단계: 주식 상승의 직접적인 원인 추출\n"
			+ "- 기사를 분석하여 주식 상승의 이유를 확인합니다. 원인과 결과가 명확히 드러나며, 사실에 기반해야 합니다.\n"
			+ "- 기사에 해당 주식이 '~~관련주'로 명시되어 있고, 해당 내용을 바탕으로 상승했다면 '~~관련주'를 테마 이름으로 사용하고 바로 결과를 출력하세요.\n"

			+ "- 주식 상승 이유에 '수혜 기대감'이 언급되었다면, 해당 수혜 기대감의 원인을 테마 이름으로 도출하세요.\n"
			+ "- 기사를 통해 다음과 같은 층위를 추출하세요:\n"
			+ "  1. **배경 지식**: 주식 상승과 관련된 광범위한 맥락.\n"
			+ "  2. **1차 원인**: 주식 상승의 구체적이고 직접적인 이유.\n"
			+ "  3. **최종 원인**: 주식 상승에 가장 직접적인 영향을 미친 핵심 요인.\n"
			+ "- 예시:\n"
			+ "  - '현대로템과 코레일이 우즈베키스탄 철도청의 고속차량 수주에 성공하며 한국산 고속차량의 해외 진출이 이루어짐.'\n"
			+ "    - 배경 지식: 해외 진출.\n"
			+ "    - 1차 원인: 현대로템과 코레일의 수주.\n"
			+ "    - 최종 원인: 철도 관련주.\n\n"

			+ "### 2단계: 테마 이름 도출\n"
			+ "- 최종 원인이 인물, 정책, 기술, 정치적 사건, 또는 산업인지에 따라 테마를 도출하는 방법이 달라집니다.\n\n"

			+ "**인물:**\n"
			+ "- **반드시 기사에 등장하는 인물 이름만 사용해야 합니다.**\n"
			+ "- 기사에서 명시적으로 언급된 경우, 인물 이름을 테마로 사용합니다. 그렇지 않다면 빈 객체를 반환하세요.\n"
			+ "- 예시:\n"
			+ "  - '한동훈 국민의힘 대표와 관련된 발언으로 주가 변동이 발생함.' → 테마명: 한동훈\n"
			+ "  - '조국 관련주가 조국의 지지율에 따라 급등락함.' → 테마명: 조국\n\n"

			+ "**정책:**\n"
			+ "- **명시되지 않은 정책 이름은 테마로 사용하지 않습니다.**\n"
			+ "- 기사에서 명시적으로 언급된 정책 이름을 테마로 사용합니다.\n"
			+ "- 예시:\n"
			+ "  - '북미정상회담으로 인해 관련 주식들이 상승함.' → 테마명: 북미정상회담\n"
			+ "  - '이재명의 지역화폐 예산 증액으로 관련 주식 상승.' → 테마명: 지역화폐\n\n"

			+ "**기술:**\n"
			+ "- 기업이 개발한 제품 또는 기술이 최종 원인이라면, 이를 테마 이름으로 사용합니다.\n"
			+ "- 기술 이름이 구체적이지 않다면, 이를 일반화하여 사용하세요.\n"
			+ "- 예시:\n"
			+ "  - '초전도체 제작 금속 소재.' → 테마명: 초전도체\n"
			+ "  - '양자 기술.' → 테마명: 양자컴퓨터\n\n"

			+ "**정치적 사건:**\n"
			+ "- 질문지에 언급된 사실만 사용하세요.\n"
			+ "- 정치적 사건은 배경 원인으로 간주하며, 최종 원인과 배경 원인을 분리해 파악하고 최종 원인을 테마로 사용하세요.\n"
			+ "- 예시:\n"
			+ "  - '탄소 중립 선언으로 친환경 에너지 관련 주식 상승.'\n"
			+ "    - 배경 원인: 탄소 중립 선언.\n"
			+ "    - 1차 원인: 친환경 에너지.\n"
			+ "    - 테마명: 친환경 에너지.\n"
			+ "  - '조기 대선 기대감으로 일자리 관련 주식 상승.'\n"
			+ "    - 배경 원인: 조기 대선.\n"
			+ "    - 1차 원인: 일자리 관련 주식.\n"
			+ "    - 테마명: 일자리 관련 주식.\n\n"

			+ "**산업:**\n"
			+ "- 기사에서 명시된 산업명을 그대로 사용합니다.\n"
			+ "- 예시:\n"
			+ "  - '코로나19로 인해 홈트레이닝 산업이 성장.' → 테마명: 홈트레이닝\n"
			+ "  - 'AI 데이터센터의 전력 수요 증가.' → 테마명: AI 데이터센터\n\n"

			+ "### 3단계: 부가적인 단어 삭제\n"
			+ "- 부가적 설명 단어를 제거하고 핵심 키워드만 남깁니다.\n"
			+ "- 예시:\n"
			+ "  - '우원식 정치적 영향력' → 우원식\n"
			+ "  - '세종시 관련 주' → 세종시\n"
			+ "  - 'AI 데이터센터 전력 수요 증가' → AI 데이터센터\n\n"

			+ "### 4단계: 테마 이름 도출 이유 설명\n"
			+ "- 테마 이름을 도출한 이유를 명확히 설명하세요.\n"
			+ "- 테마 이름이 주식 상승의 원인을 명확히 설명하는지 검토하세요.\n\n"

			+ "### 최종 테마 선정\n"
			+ "- <themas> XML에는 기존 테마가 ','로 구분되어 저장되어 있습니다.\n"
			+ "- 기존 테마와 새로 도출한 테마를 비교하여 단어 유사도가 0.6 이상이거나 의미적으로 유사하다면 기존 테마를 사용하세요.\n"
			+ "- 예시:\n"
			+ "  - 기존 테마: 양자컴퓨터, 북미정상회담. 도출된 테마: 양자암호 → 최종 테마: 양자컴퓨터.\n"
			+ "- 변경 여부와 그 이유를 명확히 설명하세요.\n";
}
