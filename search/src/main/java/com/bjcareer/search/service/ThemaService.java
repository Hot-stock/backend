package com.bjcareer.search.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bjcareer.search.domain.entity.ThemaInfo;
import com.bjcareer.search.repository.stock.ThemaInfoRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class ThemaService {
	private final ThemaInfoRepository themaInfoRepository;

	public List<ThemaInfo> searchThema(String thema) {
		log.debug("search thema: {}", thema);
		return themaInfoRepository.findByNameContains(thema);
	}
}
