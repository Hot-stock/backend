package com.bjcareer.search.out.persistence.chart;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.bjcareer.search.application.port.out.persistence.stockChart.LoadChartAboveThresholdCommand;
import com.bjcareer.search.application.port.out.persistence.stockChart.LoadChartSpecificDateCommand;
import com.bjcareer.search.application.port.out.persistence.stockChart.StockChartRepositoryPort;
import com.bjcareer.search.domain.entity.OHLC;
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
				.setParameter("date", command.getDate())
				.getSingleResult();

			return new StockChart(ohlc.getChart().getStockCode(), new ArrayList<>(List.of(ohlc)));
		} catch (NoResultException e) {
			log.warn("No findChartByDate found for {} {}", command.getDate(), command.getStockName());
			throw e;
		}
	}

	@Override
	public StockChart findOhlcAboveThreshold(LoadChartAboveThresholdCommand command) {
		return em.createQuery(Query.FIND_OHLC_ABOVE_THRESHOLD, StockChart.class)
			.setParameter("code", command.getCode())
			.setParameter("threshold", command.getThreshold())
			.getSingleResult();
	}

	@Override
	public void updateStockChartOfOHLC(StockChart stockChart) {
		log.info("Update stock chart: {}", stockChart.getStockCode());
		em.persist(stockChart);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void save(StockChart stockChart) {
		em.merge(stockChart);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void saveAll(List<StockChart> stockChart) {
		stockChart.forEach(this::save);
	}

	@Override
	public Optional<StockChart> loadStockChart(String stockCode) {

		List<StockChart> chart = em.createQuery(Query.LOAD_STOCK_CHART, StockChart.class)
			.setParameter("code", stockCode)
			.getResultList();

		return chart.isEmpty() ? Optional.empty() : Optional.of(chart.get(0));
	}

	@Override
	public List<StockChart> loadStockChartInStockCode(List<String> stockCode) {
		List<StockChart> chart = em.createQuery(
				"SELECT sc FROM StockChart sc WHERE sc.stockCode IN :code", StockChart.class
			)
			.setParameter("code", stockCode)
			.getResultList();

		return chart;
	}

}
