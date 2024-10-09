package com.bjcareer.search.out.api.gpt;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

class ChatGPTTest {
	@Test
	void testParser() throws JsonProcessingException {
		String jsonData = MockData.GPT_RESPONSE;

		ObjectMapper mapper = new ObjectMapper();
		GPTResponseDTO gptResponseDTO = mapper.readValue(jsonData, GPTResponseDTO.class);

		GPTResponseDTO.Content parsedContent = gptResponseDTO.getChoices().getFirst().getMessage().getParsedContent();

		assertEquals("그린리소스", parsedContent.getName());
		assertEquals("2024-10-01", parsedContent.getNext());
	}
}
