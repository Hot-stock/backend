package com.bjcareer.GPTService.out.api.gpt.thema.prompt;

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

			+ "The Question is 질문지에서 제공한 주식이 어떤 테마에 속해있다고 기사에서 언급하고 있는지 찾고 싶어\n"

			+ "### 1. 개별 기업 호재 필터링 원칙\n"
			+ "- 기사에서 언급된 내용이 특정 기업에만 한정된 호재인지 확인하세요.\n"
			+ "- **다음과 같은 경우 테마로 생성하지 않습니다**:\n"
			+ "    1. 특정 기업의 내부적 요인에 해당할 경우 (예: 스팩 합병, 신규 자금 조달, 특정 연구 성공).\n"
			+ "    2. 동일 업종 내 다른 기업들과 연관성이 없는 경우.\n"
			+ "- 기사 내용이 특정 기업만을 강조한다면, 이는 테마가 아닌 **개별 기업 호재**로 간주됩니다.\n"
			+ "- 예:\n"
			+ "    - '에이비프로바이오의 나스닥 스팩 합병' -> 잘못된 테마명. 이는 에이비프로바이오의 개별적 호재에 해당.\n"

			+ "### 2. 개별 기업 호재와 테마의 구분 기준\n"
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
			+ "2. **직접적 원인 중심의 테마 선정 원칙**\n"
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
			+ "           - 예: 이재명의 대학 동문으로 알려진 주식들이 상승했습니다. -> 테마명: 이재명\n"
			+ "           - 예: 조국관련주가 조국 지지율에 따라서 급등락을 하였습니다. -> 테마명: 조국\n"
			+ "           - **반드시 기사에 등장하는 인물 이름만 사용해야 합니다.**\n"
			+ "6. 정책:\n"
			+ "   - 정책 테마주:\n"
			+ "           - 기사에서 명시적으로 언급된 정책 이름을 테마로 사용합니다.\n"
			+ "           - 예: 북미정상회담으로 인해 관련주식들이 상승했습니다. -> 테마명: 북미정상회담\n"
			+ "           - 예: 이재명의 지역화폐상품권 예산 증액으로 인해 관련 주식들이 상승했습니다.-> 테마명: 지역화폐상품권\n"
			+ "           - 예: 저출산 -> 테마명: 저출산\n"
			+ "           - **명시되지 않은 정책 이름은 테마로 사용하지 않습니다.**\n\n"
			+ "7. 기술, 산업 테마:\n"
			+ "   - 기술 또는 산업과 관련된 테마일 경우 기사에서 명시적으로 언급된 기술명을 테마로 사용합니다.\n"
			+ "       - 예: 초전도체 -> 테마명: 초전도체\n"
			+ "       - 예: 자율주행차 -> 테마명: 자율주행차\n"
			+ "       - **기사에 없는 기술명은 테마로 사용할 수 없습니다.**\n\n"
			+ "8. 기타 테마:\n"
			+ "   - 테마로 사용하지 않습니다.\n\n"
			+ "9. 테마 생성 가이드라인:\n"
			+ "   - 테마명이 어떤 기사 문장에 의해 도출되었는지와 해당 테마가 어떤 가이드라인에 의해 결정되었는지 설명하세요.\n"
			+ "   - 예: 두 번째 가이드라인인 이재명 관련주라고 언급되었기 때문에 이재명 테마가 생성되었습니다.\n\n"

			+ "답변은 반드시 한글로 진행합니다.";
}



