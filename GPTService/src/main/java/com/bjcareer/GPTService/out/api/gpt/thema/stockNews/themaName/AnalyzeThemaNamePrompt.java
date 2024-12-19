package com.bjcareer.GPTService.out.api.gpt.thema.stockNews.themaName;

public class AnalyzeThemaNamePrompt {
	public static final String PROMPT =
		"테마 이름 후보: <themaName>%s</themaName> \n"
			+ "테마가 추출된 이유: <reason>%s</reason> \n"
			+ "알고있는 테마들: <themas>%s</themas> "

			+ "Act like an investor specializing in theme stocks. Analyze the provided article to identify the reasons behind the stock price increases, categorize them into investment themes, and organize these themes in a database for effective management and tracking."
			+ "Based on those stocks, think of a strategy to extract detailed themes from articles for theme stock investment.\n"
			+ "Generate three ideas and evaluate each theme's representativeness, informational value, and reusability."
			+ "and encourage students to learn more about theme stocks. Use 3 different platforms and methods to reach thema.\n\n"

			+ "Generate 3 ideas and evaluate how well each theme represents the stocks.\n"
			+ "While doing this, assess how effective each idea is in connecting students with theme stocks, "
			+ "its feasibility, and how engaging it is for the target audience.\n\n"

			+ "질문: reason 필드를 보고 테마 이름을 획득하고 싶어.\n"

			+ "가이드라인\n"

			+ "1. **주식 상승의 1차원인을 획득"
			+ " 주식 상승의 1차 원인을 확인하고, 인물, 제품, 사건의 명칭을 획득합니다..\n"
			+ "예) 리가켐바이오 항암제'라는 구체적인 이름이 더 주가 상승의 원인을 명확하게 설명합니다. 이는 리가켐바이오의 특정 항암제 LCB71의 긍정적인 임상 결과가 주가 상승의 주요 원인이 되었기 때문입니다. -> 상승원인: 항암제 LCB71\n"
			+ "예) 유한양행의 레이저티닙(렉라자)의 글로벌 파트너십 및 FDA 허가 -> 레이저티닙(렉라자)\n"
			+ "예) 우원식 국회의장의 정치적 영향력이 주된 요인으로 작용했습니다. -> 우원식\n"

			+ "2. **명확한 키워드 사용"
			+ " 테마 이름은 주식 상승의 원인을 명확하게 설명하는 단어여야 합니다.\n"
			+ "   - 예: '신약 개발' → '알츠하이머 신약', '재건 사업' → '우크라이나 재건', '정책 호재' → '트럼프 인프라 정책'\n"

			+ "예): 우원식 정치적 영향력 -> 우원식"
			+ "예): 세종시 관련주 -> 세종시"
			+ "예): 세종시 국회 이전 -> 국회이전"
			+ "4. 인물:\n"
			+ "   - 인물 테마주:\n"
			+ "           - 기사에서 명시적으로 관련이 됐다고 언급한 경우 '인물 이름'을 테마로 사용합니다. 그렇지 않다면 빈 객체를 반환합니다.\n"
			+ "           - 예: 한동훈 국민의힘 대표와의 인적 네트워크 및 관련 발언으로 인해 주가 변동이 발생한 관련주들로 분류됨. -> 테마명: 한동훈\n"
			+ "           - 예: 조국관련주가 조국 지지율에 따라서 급등락을 하였습니다. -> 테마명: 조국\n"
			+ "           - **반드시 기사에 등장하는 인물 이름만 사용해야 합니다.**\n"
			+ "5. 정책:\n"
			+ "   - 정책 테마주:\n"
			+ "           - 기사에서 명시적으로 언급된 정책 이름을 테마로 사용합니다.\n"
			+ "           - 예: 북미정상회담으로 인해 관련주식들이 상승했습니다. -> 테마명: 북미정상회담\n"
			+ "           - 예: 이재명의 지역화폐상품권 예산 증액으로 인해 관련 주식들이 상승했습니다.-> 테마명: 지역화폐상품권\n"
			+ "           - 예: 새로운 발언에 따라 세종시 인근에 본사와 공장 부지를 둔 기업들이 주목받고 있어 세종시 테마주로 엮임 -> 테마명: 세종시\n"
			+ "           - **명시되지 않은 정책 이름은 테마로 사용하지 않습니다.**\n\n"
			+ "7. 기술이름:\n"
			+ "   - 기술 테마주:\n"
			+ "           - 기사에서 명시적으로 언급된 기술 이름을 사용하되 일반적인 기술이름을 사용합니다.\n"
			+ "           - 예: 초전도체 제작 금속 소재. -> 테마명: 초전도체\n"
			+ "6. **부가적인 단어 삭제**"
			+ " 테마의 이름은 1~3단어로 고정합니다. 부가적인 단어는 제거합니다 .부가적인 단어란 한단어로 대표할 수 있는데 뒤에 추가적으로 붙는 어미를 뜻합니다.\n"
			+ "초전도체 개발 -> 초전도체"
			+ "우원식 관련주 -> 우원식"
			+ "CCUS 기술 -> CCUS"

			+ "6. **도출 이유 설명**"
			+ " 테마 이름을 도출한 이유를 명확하게 설명하세요. 테마 이름이 주식 상승의 원인을 명확하게 설명하는지 확인하세요.\n"

		+ "**최종 테마 선정**"
		+ "<themas> </themas>에는 이전부터 사용한 테마 내역들이 있고 ','로 구분되어 있어.\n"
		+ "가이드라인에서 설정한 테마들과 기존의 테마들을 비교하여 유사도가 0.6이상이거나 맥락적 의미가 비슷하다면 해당 테마로 변경 해.\n"
		+ "유사도가 0.6이상이 없으면 기존의 테마를 사용해.\n"
		+ "테마 이름이 어떻게 변경됐는지 이유와 변경되지 않았다면 해당 이유도 설명해야해";


}
