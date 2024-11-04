package com.bjcareer.search.domain.entity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.BatchSize;

import com.bjcareer.search.domain.News;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Stock {
	@Id
	@GeneratedValue
	@Column(name="STOCK_ID")
	private Long id;

	@Column(unique = true)
	private String code;
	private String name;

	@Enumerated(EnumType.STRING)
	private Market market;
	private String href;

	private int issuedShares;
	private int price;
	private Long marketCapitalization;

	@OneToOne(mappedBy = "stock", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private StockChart stockChart;

	@OneToMany(mappedBy = "stock", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@BatchSize(size = 100)
	private List<Thema> themas = new ArrayList<>();

	@OneToMany(mappedBy = "stock", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@BatchSize(size = 10)
	private List<StockRaiseReasonEntity> raiseReasons = new ArrayList<>();

	public Stock(String code, String name, Market market, String href, int issuedShares, int price) {
		this.code = code;
		this.name = name;
		this.market = market;
		this.href = href;
		this.issuedShares = issuedShares;
		this.price = price;

		if (issuedShares != 0 && price != 0) {
			this.marketCapitalization = (long)issuedShares * price;
		}else {
			this.marketCapitalization = 0L;
		}
	}

	public Stock(String code, String name) {
		this(code, name, null, null, 0, 0);
	}

	public LocalDate calculateStartDayForUpdateStockChart() {
		if (stockChart == null || stockChart.getOhlcList().isEmpty()) {
			return LocalDate.of(1999, 1, 1); // '1999-01-01' 날짜 생성
		}

		LocalDate date = stockChart.getOhlcList().getLast().getDate();
		return date.plusDays(1);
	}

	public void mergeStockChart(StockChart stockChart) {
		if (this.stockChart == null) {
			this.stockChart = stockChart;
		} else {
			this.stockChart.addOHLC(stockChart.getOhlcList());
		}

		this.stockChart.setStock(this);
	}

	public void updateStockInfo(Stock stock) {
		this.name = stock.getName();
		this.issuedShares = stock.getIssuedShares();
		this.price = stock.getPrice();
		this.marketCapitalization = stock.getMarketCapitalization();
	}
}
