package com.bjcareer.search.out.api.gpt;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class MockData {
	public static final String GPT_RESPONSE =""
		+ "{\n"
		+ "  \"id\": \"chatcmpl-AGMLivzS1dXZZXBaJV94GXyd1C5gP\",\n"
		+ "  \"object\": \"chat.completion\",\n"
		+ "  \"created\": 1728462522,\n"
		+ "  \"model\": \"gpt-4o-2024-08-06\",\n"
		+ "  \"choices\": [\n"
		+ "    {\n"
		+ "      \"index\": 0,\n"
		+ "      \"message\": {\n"
		+ "        \"role\": \"assistant\",\n"
		+ "        \"content\": \"{\\\"name\\\":\\\"그린리소스\\\",\\\"reason\\\":\\\"그린리소스가 58억원 규모의 장비 공급 계약을 체결하며 주가가 상승했다. 이는 초전도선재와 핵융합 발전 프로젝트 참여 기대감이 반영된 것이다. 초전도선재는 전력 전송 효율이 높아 신재생 에너지 분야에서 수요가 증가할 것으로 예상된다.\\\",\\\"thema\\\":\\\"초전도, 신재생 에너지\\\",\\\"next\\\":\\\"2024-10-01\\\",\\\"next_reason\\\":\\\"그린리소스의 IBAD 시스템 장비 공급계약은 2024년 10월부터 시작될 예정이다. 이는 해당 장비가 초전도선재 제작에 필수적이기 때문이다.\\\"}\",\n"
		+ "        \"refusal\": null\n"
		+ "      },\n"
		+ "      \"logprobs\": null,\n"
		+ "      \"finish_reason\": \"stop\"\n"
		+ "    }\n"
		+ "  ],\n"
		+ "  \"usage\": {\n"
		+ "    \"prompt_tokens\": 2634,\n"
		+ "    \"completion_tokens\": 158,\n"
		+ "    \"total_tokens\": 2792,\n"
		+ "    \"prompt_tokens_details\": {\n"
		+ "      \"cached_tokens\": 2432\n"
		+ "    },\n"
		+ "    \"completion_tokens_details\": {\n"
		+ "      \"reasoning_tokens\": 0\n"
		+ "    }\n"
		+ "  },\n"
		+ "  \"system_fingerprint\": \"fp_e5e4913e83\"\n"
		+ "}";
}
