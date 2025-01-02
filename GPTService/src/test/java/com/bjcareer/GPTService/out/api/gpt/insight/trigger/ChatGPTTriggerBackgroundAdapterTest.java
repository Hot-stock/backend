package com.bjcareer.GPTService.out.api.gpt.insight.trigger;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.bjcareer.GPTService.application.AnalyzeRaiseBackground;
import com.bjcareer.GPTService.domain.gpt.GPTTriggerBackground;

@SpringBootTest
class ChatGPTTriggerBackgroundAdapterTest {
	@Autowired
	private GPTTriggerAdapter GPTTriggerAdapter;
	@Autowired
	private AnalyzeRaiseBackground analyzeRaiseBackground;

	@Test
	void test() {
		String thema = "양자컴퓨터";
		Optional<GPTTriggerBackground> trigger1 = analyzeRaiseBackground.saveTriggerOfRiseOfThema(thema, "우리넷");

	}
}
