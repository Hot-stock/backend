package com.bjcareer.search.event;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.bjcareer.search.service.RankingService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class SearchKeywordHandler {
	private final RankingService rankingService;

	@EventListener
	public void handle(SearchedKeyword event) {
		log.debug("SearchKeywordHandler: {}", event.getKeyword());
		System.out.println("event asdasdasd= " + event.getKeyword());
		rankingService.updateKeyword(event.getKeyword());
	}
}
