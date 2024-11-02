package com.bjcareer.search.domain.entity;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.BatchSize;

import com.bjcareer.search.domain.News;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Getter
public class StockChart {
	@Id
	@GeneratedValue
	@Column(name = "stock_chart_id")
	private Long id;

	@OneToOne
	@JoinColumn(name = "stock_id", unique = true) // 외래 키 매핑
	private Stock stock;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "chart", cascade = CascadeType.ALL)
	@BatchSize(size = 100)
	private List<OHLC> ohlcList = new ArrayList<>();

	public StockChart(Stock stock, List<OHLC> ohlcList) {
		this.stock = stock;
		addOHLC(ohlcList);
	}

	public void mapNewsToOHLC(News news) {
		ohlcList.stream()
			.filter(ohlc -> ohlc.getDate() == news.getPubDate())
			.findFirst()
			.ifPresent(ohlc -> ohlc.addRoseNews(news));
	}

	public List<OHLC> getIncreaseReason() {
		return ohlcList.stream().filter(ohlc -> ohlc.getNews() != null).toList();
	}

	public void addOHLC(List<OHLC> ohlcs) {
		ohlcs.stream().forEach(ohlc -> ohlc.addChart(this));
		this.ohlcList.addAll(ohlcs);
	}

	public void setStock(Stock stock) {
		this.stock = stock;
	}
}
