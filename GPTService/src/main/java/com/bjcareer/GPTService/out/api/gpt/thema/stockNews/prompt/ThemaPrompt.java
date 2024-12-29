package com.bjcareer.GPTService.out.api.gpt.thema.stockNews.prompt;

public class ThemaPrompt {
	public static final String PROMPT =
		"Act like an investor specializing in theme stocks. Analyze the provided article to identify the reasons behind the stock price increases, categorize them into investment themes, and organize these themes in a database for effective management and tracking."
			+ "Based on those stocks, think of a strategy to extract detailed themes from articles for theme stock investment.\n"
			+ "Generate three ideas and evaluate each theme's representativeness, informational value, and reusability."
			+ "and encourage students to learn more about theme stocks. Use 3 different platforms and methods to reach thema.\n\n"

			+ "Generate 3 ideas and evaluate how well each theme represents the stocks.\n"
			+ "While doing this, assess how effective each idea is in connecting students with theme stocks, "
			+ "its feasibility, and how engaging it is for the target audience.\n\n"

			+ "Refine each idea to make the educational strategies clearer and more structured. \n"
			+ "Carefully analyze the pros and cons of each activity. Break down the educational challenge into smaller parts, \n"
			+ "evaluate possible solutions, and select the most effective strategy based on your findings.\n\n"

			+ "The Question is 질문지에서 제공한 주식은 어떤 테마가 될 수 있는지 알 고 싶어\n"

			+ "### 1. 개별 기업 호재와 테마의 구분 기준\n"
			+ "- 기사 내용이 여러 기업과 연관되어 산업적 흐름이나 기술적 트렌드를 나타내는 경우에만 테마로 간주합니다.\n"
			+ "- 특정 기업의 개별적 성공 사례는 테마로 사용하지 않습니다.\n"
			+ "- 다음 질문을 통해 테마로 적합한지 판단하세요:\n"
			+ "    1. 기사에서 언급된 사건이 특정 기업 외에 다른 기업에도 영향을 미치는가?\n"
			+ "    2. 해당 사건이 동일 업종의 다른 기업들에게도 유사한 영향을 줄 가능성이 있는가?\n"
			+ "    3. 사건이 특정 산업, 기술, 정책과 관련되어 광범위한 영향을 미치는가?\n"
			+ "- 위 질문에 '아니오'가 포함되면 이는 테마가 아닌 개별 기업 호재로 간주됩니다. 빈 객체를 반환하고 종료합니다.\n\n"
			+ "_위 질문이 '예'라면, 왜 예가 나왔는지 이유를 설명하고 다음 3가지 카테고리를 기반으로 기사의 테마를 판별하고 테마명을 결정하세요.\n\n"

			+ "다음 3가지 카테고리를 기반으로 기사의 테마를 판별하고 테마명을 결정하세요.\n\n"
			+ "1. **명시적 단어 사용 원칙**:\n"
			+ "    - 기사에서 언급된 단어를 그대로 테마명으로 사용해야 합니다.\n"
			+ "    - 기사에 명시적으로 존재하지 않는 단어나 추론된 개념은 테마명으로 사용할 수 없습니다.\n\n"
			+ "2. 직접적 원인 중심의 테마 선정 원칙\n"
			+ "- 주가 상승의 직접적인 원인(1차 원인)을 테마명으로 설정해야 합니다.\n"
			+ "- 배경이 되는 간접적 요인(2차 원인)이나 연관된 일반적 맥락은 테마명으로 사용할 수 없습니다.\n"
			+ "- **테마명은 기사에서 명시적으로 언급된 단어**를 그대로 사용해야 합니다.\n"
			+ "- 예:\n"
			+ "    - 윤석열 탄핵 사건으로 인하여 우원식 관련주가 상승했다 -> 테마명: 우원식(주가 상승의 직접적 원인인 인물)\n"
			+ "3. **테마명 선정 기준 강화**:\n"
			+ "    - 주식의 상승 원인과 관련된 명확한 키워드를 기반으로 테마명을 선정해야 합니다.\n"
			+ "    - 기사에서 언급된 원인들이 다중으로 연결된 경우, **직접적인 원인에 해당하는 테마명**만을 선택합니다.\n\n"
			+ "4. 기업 개별 호재:\n"
			+ "     - 테마로 사용하지 않습니다.\n\n"
			+ "5. 인물:\n"
			+ "   - 인물 테마주:\n"
			+ "           - 기사에서 명시적으로 관련이 됐다고 언급한 경우 '인물 이름'을 테마로 사용합니다. 그렇지 않다면 빈 객체를 반환합니다.\n"
			+ "           - 예: 한동훈 국민의힘 대표와의 인적 네트워크 및 관련 발언으로 인해 주가 변동이 발생한 관련주들로 분류됨. -> 테마명: 한동훈\n"
			+ "           - 예: 조국관련주가 조국 지지율에 따라서 급등락을 하였습니다. -> 테마명: 조국\n"
			+ "           - **반드시 기사에 등장하는 인물 이름만 사용해야 합니다.**\n"
			+ "6. 정책:\n"
			+ "   - 정책 테마주:\n"
			+ "           - 기사에서 명시적으로 언급된 정책 이름을 테마로 사용합니다.\n"
			+ "           - 예: 북미정상회담으로 인해 관련주식들이 상승했습니다. -> 테마명: 북미정상회담\n"
			+ "           - 예: 이재명의 지역화폐상품권 예산 증액으로 인해 관련 주식들이 상승했습니다.-> 테마명: 지역화폐상품권\n"
			+ "           - 예: 새로운 발언에 따라 세종시 인근에 본사와 공장 부지를 둔 기업들이 주목받고 있어 세종시 테마주로 엮임 -> 테마명: 세종시\n"
			+ "           - **명시되지 않은 정책 이름은 테마로 사용하지 않습니다.**\n\n"
			+ "7. 기술, 산업 테마:\n"
			+ "   - 기술 또는 산업과 관련된 테마일 경우 기사에서 명시적으로 언급된 기술명을 테마로 사용합니다.\n"
			+ "       - 예: 자율주행차 -> 테마명: 자율주행차\n"
			+ "       - **기사에 없는 기술명은 테마로 사용할 수 없습니다.**\n\n"
			+ "8. 공급:\n"
			+ "  - 공급 테마주:\n"
			+ "           - 기사에서 명시적으로 언급된 고객사 이름을 사용합니다.\n"
			+ "예: 삼성전자의 LG디스플레이 납품 소식으로 관련주들이 상승했습니다. -> 테마명: LG디스플레이\n"
			+ "9. 기타 테마:\n"
			+ "   - 위의 3가지 카테고리에 속하지 않는 경우 테마로 사용하지 않습니다.\n\n"

			+ "### 추출된 테마 평가\n"
			+ "생성된 테마를 평가하고 필터링하세요:\n"
			+ "1. **평가 기준**:\n"
			+ "   - 대표성(representativeness): 테마의 이름이 주가 상승의 직접적인 원인의 명사적 단어거나, 직접적인 제품명이나  인물인가?\n"
			+ "   - 정보적 가치(informational value): 테마의 이름이 직관적인가?. 명사적 단어일 수록 높은 점수를 부여\n"
			+ "   - 재사용 가능성(reusability): 테마의 이름으로 인터넷에 검색하면 해당 테마의 추가적인 정보 및 일정 획득이 가능한가?\n"
			+ "2. **평가 방식**:\n"
			+ "   - 각 테마를 1~10점으로 평가하고, 평균 점수가 6점 미만인 테마는 제외하세요.\n\n"

			//평가 단계
			+ "### 6. Evaluate 단계\n"
			+ "테마의 신뢰성과 중복을 검증하세요:\n"
			+ "  **대표성(representativeness)**: 테마의 이름이 주가 상승의 직접적인 원인의 명사적 단어거나, 직접적인 제품명이나  인물인가?\n"
			+ "  예: '리쥬란'은 '글로벌 스킨부스터'라는 테마명으로 대표성이 있는가?\n"
			+ "- **최종 테마 결정**:\n"
			+ "  - 동일 테마가 3회 이상 반복되면 최종 리스트에 추가.\n"
			+ "  - 테마 간 단어가 중복되지 않는 유니크 페어를 유지합니다.\n\n"


			+ "답변은 반드시 한글로 진행합니다.";
}



