package com.bjcareer.search.domain.entity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.hibernate.annotations.BatchSize;

import com.bjcareer.search.config.AppConfig;
import com.bjcareer.search.domain.GTPNewsDomain;
import com.fasterxml.jackson.core.JsonProcessingException;

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
import lombok.extern.slf4j.Slf4j;

@Entity
@NoArgsConstructor
@Getter
@Slf4j
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
		this.ohlcList = ohlcList;
	}

	public Optional<GTPNewsDomain> loadNewByDate(LocalDate date) {
		OHLC ohlc = getSameDateOHLC(date);
		if (ohlc == null || ohlc.getNews().isNull()) {
			return Optional.empty();
		}

		return Optional.of(convertJsonTypeToObject(ohlc));
	}

	public List<GTPNewsDomain> getAllNews() {
		List<GTPNewsDomain> news = new ArrayList<>();

		for (OHLC ohlc : ohlcList) {
			if (ohlc.getNews() != null) {
				news.add(convertJsonTypeToObject(ohlc));
			}
		}

		return news;
	}

	public void addNewsToOhlc(GTPNewsDomain news, LocalDate date) {
		getSameDateOHLC(date).addRoseNews(news);
	}

	public void addOHLC(List<OHLC> ohlcs) {
		for (OHLC ohlc : ohlcs) {
			ohlc.addChart(this);
			this.ohlcList.add(ohlc);
		}
	}

	private GTPNewsDomain convertJsonTypeToObject(OHLC ohlc) {
		GTPNewsDomain gtpNewsDomain = null;
		try {
			gtpNewsDomain = AppConfig.customObjectMapper()
				.treeToValue(ohlc.getNews(), GTPNewsDomain.class);
		} catch (JsonProcessingException e) {
			log.warn("Fail to convert JsonNode to News {}", e.getMessage());
		}

		return gtpNewsDomain;
	}

	private OHLC getSameDateOHLC(LocalDate date) {
		for (OHLC ohlc : ohlcList) {
			if (ohlc.getDate().equals(date)) {
				return ohlc;
			}
		}
		return null;
	}

	public void setStock(Stock stock) {
		this.stock = stock;
	}

}
