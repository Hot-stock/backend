package com.bjcareer.GPTService.application;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.bjcareer.GPTService.domain.gpt.GPTTriggerBackground;

@SpringBootTest
class AnalyzeRaiseBackgroundTest {
	@Autowired
	private AnalyzeRaiseBackground analyzeRaiseBackground;

	@Test
	void 테마를_분석하고_해당_내용이_저장되어_있어야_함() {
		Optional<GPTTriggerBackground> trigger = analyzeRaiseBackground.saveTriggerOfRiseOfThema("천연가스", "태광");
		assertTrue(trigger.isPresent());
	}
}
