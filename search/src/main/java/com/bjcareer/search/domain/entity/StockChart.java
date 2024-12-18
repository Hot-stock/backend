package com.bjcareer.search.domain.entity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.BatchSize;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Transient;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Entity
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@Getter
@Slf4j
public class StockChart {
	@Id
	@GeneratedValue
	@Column(name = "stock_chart_id")
	private Long id;

	@Column(name = "stock_code", nullable = false, unique = true)
	private String stockCode;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "chart", cascade = CascadeType.ALL)
	@BatchSize(size = 100)
	@OrderBy("date ASC")
	private List<OHLC> ohlcList = new ArrayList<>();

	@Column(name = "last_update_date", nullable = false)
	private LocalDate lastUpdateDate = LocalDate.now().minusYears(1);

	@Transient
	private Stock stock;

	public StockChart(String stockCode, List<OHLC> ohlcList) {
		this.stockCode = stockCode;
		addOHLC(ohlcList);
	}

	public Double calcMovingAverageOfIncrease(int performance) {
		if (ohlcList == null || ohlcList.isEmpty() || performance <= 0) {
			log.warn("Invalid input data or performance");
			return 0.0;
		}

		double sum = 0.0; // 소수점 연산을 위해 double 타입 사용
		int count = 0;

		for(int i = 0; i < performance; ++i){
			sum += ohlcList.get(i).getPercentageIncrease();
			count++;
		}

		// 평균을 계산
		if (count == 0) {
			return 0.0; // 데이터가 부족한 경우
		}

		double movingAverage = sum / count;

		// 소수점 두 자리로 반환
		return Math.round(movingAverage * 100.0) / 100.0;
	}

	public void addOHLC(List<OHLC> ohlcs) {
		for (OHLC ohlc : ohlcs) {
			ohlc.addChart(this);
			this.ohlcList.add(ohlc);
		}
	}

	public void mergeOhlc(StockChart stockChart) {
		for (OHLC stockOhlc : stockChart.getOhlcList()) {
			if (this.getLastUpdateDate().isBefore(stockOhlc.getDate())) {
				lastUpdateDate = stockOhlc.getDate();
				stockOhlc.addChart(this);
				ohlcList.add(stockOhlc);
			}
		}
	}

	public LocalDate getLastUpdateDate() {
		if (lastUpdateDate == null) {
			return LocalDate.now().minusYears(1);
		} else {
			return lastUpdateDate;
		}

	}
}
