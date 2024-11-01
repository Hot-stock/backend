package com.bjcareer.search.domain.entity;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.hibernate.annotations.BatchSize;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
public class StockChart {
	@Id
	@GeneratedValue
	private Long id;

	@OneToMany
	@BatchSize(size = 100)
	List<OHLC> ohlcList = new ArrayList<>();

	public StockChart(List<OHLC> ohlcList) {
		this.ohlcList = ohlcList;
	}

	public List<OHLC> getOHLCsAboveThreshold(int threshold) {
		List<OHLC> target = new ArrayList<>();

		for (OHLC ohlc : ohlcList) {
			if (ohlc.getPercentageIncrease() >= threshold) {
				target.add(ohlc);
			}
		}

		target.sort(Comparator.comparing(OHLC::getDate));
		return target;
	}
}
