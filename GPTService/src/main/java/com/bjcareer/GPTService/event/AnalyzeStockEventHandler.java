package com.bjcareer.GPTService.event;

import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.bjcareer.GPTService.application.GPTStockAnalyzeService;
import com.bjcareer.GPTService.event.command.AnalyzeStockEvent;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RequiredArgsConstructor
public class AnalyzeStockEventHandler {
	private final GPTStockAnalyzeService gptStockAnalyzeService;

	@Async
	@EventListener
	public void handle(AnalyzeStockEvent event) {
		log.debug("AnalyzeStockEventHandler handle event: {}", event);
		gptStockAnalyzeService.analyzeStockNewsByDateWithStockName(event.getStartDate(), event.getStockName());
	}
}
