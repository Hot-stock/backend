package com.bjcareer.search.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.bjcareer.search.domain.entity.Thema;
import com.bjcareer.search.repository.stock.ThemaRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class StockService {
	private final ThemaRepository themaRepository;

	public List<Thema> getStockOfThema(String stock) {
		log.debug("stock: {}", stock);
		return themaRepository.findByStockName(stock);
	}
}
