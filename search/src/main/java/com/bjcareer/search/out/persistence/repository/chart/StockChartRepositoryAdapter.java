package com.bjcareer.search.out.persistence.repository.chart;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.bjcareer.search.application.port.out.persistence.stockChart.LoadChartAboveThresholdCommand;
import com.bjcareer.search.application.port.out.persistence.stockChart.LoadChartSpecificDateCommand;
import com.bjcareer.search.application.port.out.persistence.stockChart.StockChartRepositoryPort;
import com.bjcareer.search.domain.entity.OHLC;
import com.bjcareer.search.domain.entity.Stock;
import com.bjcareer.search.domain.entity.StockChart;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import lombok.extern.slf4j.Slf4j;

@Repository
@Slf4j
public class StockChartRepositoryAdapter implements StockChartRepositoryPort {
	@PersistenceContext
	private EntityManager em;

	@Override
	public StockChart findChartByDate(LoadChartSpecificDateCommand command) {
		try {
			OHLC ohlc = em.createQuery(Query.FIND_CHART_BY_DATE, OHLC.class)
				.setParameter("stockName", command.getStockName())
				.setParameter("date", command.getDate())
				.getSingleResult();

			return new StockChart(ohlc.getChart().getStock(), new ArrayList<>(List.of(ohlc)));
		} catch (NoResultException e) {
			log.warn("No findChartByDate found for {} {}", command.getDate(), command.getStockName());
		}

		return new StockChart();
	}

	@Override
	public StockChart findOhlcAboveThreshold(LoadChartAboveThresholdCommand command) {
		Stock stock = em.createQuery(Query.FIND_STOCK_BY_CODE, Stock.class)
			.setParameter("code", command.getCode())
			.getSingleResult();

		List<OHLC> resultList = em.createQuery(Query.FIND_OHLC_ABOVE_THRESHOLD, OHLC.class)
			.setParameter("code", command.getCode())
			.setParameter("threshold", command.getThreshold())
			.getResultList();

		return new StockChart(stock, resultList);
	}

	@Override
	public void updateStockChartOfOHLC(StockChart stockChart) {
		stockChart.getOhlcList().forEach(em::persist);
	}

	@Override
	@Transactional
	public void save(StockChart stockChart) {
		em.persist(stockChart);
	}

	@Override
	public Optional<StockChart> loadStockChart(String stockCode) {
		List<StockChart> code = em.createQuery(Query.LOAD_STOCK_CHART, StockChart.class)
			.setParameter("code", stockCode)
			.getResultList();

		return code.isEmpty() ? Optional.empty() : Optional.of(code.get(0));
	}
}
