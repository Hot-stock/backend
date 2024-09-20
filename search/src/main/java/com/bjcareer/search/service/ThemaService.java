package com.bjcareer.search.service;

import java.util.List;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bjcareer.search.domain.entity.ThemaInfo;
import com.bjcareer.search.event.SearchedKeyword;
import com.bjcareer.search.repository.stock.ThemaInfoRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class ThemaService {
	private final ThemaInfoRepository themaInfoRepository;
	private final ApplicationEventPublisher eventPublisher;

	public List<ThemaInfo> searchThema(String thema) {
		List<ThemaInfo> result = themaInfoRepository.findByNameContains(thema);

		if (!result.isEmpty()) {
			eventPublisher.publishEvent(new SearchedKeyword(thema));
		}
		return result;
	}
}
