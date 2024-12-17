package com.bjcareer.search.application;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bjcareer.search.application.helper.ThemaCalculatorHelper;
import com.bjcareer.search.application.port.out.persistence.stockChart.StockChartRepositoryPort;
import com.bjcareer.search.application.port.out.persistence.thema.ThemaRepositoryPort;
import com.bjcareer.search.domain.HitMapDomain;
import com.bjcareer.search.domain.entity.Stock;
import com.bjcareer.search.domain.entity.StockChart;
import com.bjcareer.search.domain.entity.Thema;
import com.bjcareer.search.domain.entity.ThemaInfo;
import com.bjcareer.search.out.persistence.RedisHitMapAdapter;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class HitMapService {
	private final ThemaRepositoryPort themaRepositoryPort;
	private final StockChartRepositoryPort stockChartRepositoryPort;
	private final RedisHitMapAdapter redisHitMapAdapter;

	@Transactional(readOnly = true)
	public List<HitMapDomain> calcHitMap(Integer avgDay) {
		List<HitMapDomain> result = new ArrayList<>();

		List<Thema> themas = themaRepositoryPort.findAll();
		Map<ThemaInfo, List<Stock>> groupingThema = ThemaCalculatorHelper.groupStocksUsingThema(themas);

		for (ThemaInfo themaInfo : groupingThema.keySet()) {
			Map<Stock, StockChart> chartMap = new HashMap<>();
			List<Stock> stocks = groupingThema.get(themaInfo);

			for (Stock stock : stocks) {
				Optional<StockChart> stockChart = stockChartRepositoryPort.loadStockChart(stock.getCode());
				stockChart.ifPresent(chart -> chartMap.put(stock, chart));
			}

			result.add(new HitMapDomain(themaInfo, chartMap, avgDay));
		}

		return result;
	}
}
