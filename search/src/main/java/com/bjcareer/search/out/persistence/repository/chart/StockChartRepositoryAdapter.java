package com.bjcareer.search.out.persistence.repository.chart;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.bjcareer.search.domain.entity.OHLC;
import com.bjcareer.search.domain.entity.Stock;
import com.bjcareer.search.domain.entity.StockChart;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import lombok.extern.slf4j.Slf4j;

@Repository
@Slf4j
public class StockChartRepositoryAdapter {
	@PersistenceContext
	private EntityManager em;

	@Transactional(readOnly = true)
	public StockChart findOhlcAboveThreshold(String code, int threshold) {
		Stock stock = em.createQuery(StockChartQuery.FIND_STOCK_BY_CODE, Stock.class)
			.setParameter("code", code)
			.getSingleResult();

		List<OHLC> resultList = em.createQuery(StockChartQuery.FIND_OHLC_ABOVE_THRESHOLD, OHLC.class)
			.setParameter("code", code)
			.setParameter("threshold", threshold)
			.getResultList();

		return new StockChart(stock, resultList);
	}

	@Transactional(readOnly = true)
	public StockChart findChartByDate(String stockName, LocalDate date) {
		try {
			OHLC ohlc = em.createQuery(StockChartQuery.FIND_CHART_BY_DATE, OHLC.class)
				.setParameter("stockName", stockName)
				.setParameter("date", date)
				.getSingleResult();

			return new StockChart(ohlc.getChart().getStock(), new ArrayList<>(List.of(ohlc)));
		} catch (NoResultException e) {
			log.warn("No findChartByDate found for {} {}", stockName, date);
		}

		return new StockChart();
	}

	@Transactional
	public void updateStockChartOfOHLC(StockChart stockChart) {
		stockChart.getOhlcList().forEach(em::persist);
	}

	@Transactional
	public void save(StockChart stockChart) {
		em.persist(stockChart);
	}
}
