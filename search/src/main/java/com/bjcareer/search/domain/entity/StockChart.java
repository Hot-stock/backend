package com.bjcareer.search.domain.entity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.hibernate.annotations.BatchSize;

import com.bjcareer.search.config.AppConfig;
import com.bjcareer.search.domain.GTPNewsDomain;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.NoResultException;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Transient;
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

	@Column(name = "stock_id", nullable = false, unique = true)
	private Long stockId;

	@Transient
	private Stock stock;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "chart", cascade = CascadeType.ALL)
	@BatchSize(size = 100)
	private List<OHLC> ohlcList = new ArrayList<>();

	public StockChart(Stock stock, List<OHLC> ohlcList) {
		this.stock = stock;
		this.ohlcList = ohlcList;
	}

	public List<GTPNewsDomain> loadNewByDate(LocalDate date) {
		OHLC ohlc = getSameDateOHLC(date);
		return convertJsonTypeToObject(ohlc);
	}

	public List<GTPNewsDomain> getNextSchedule(LocalDate baseDate) {
		List<GTPNewsDomain> news = new ArrayList<>();

		for (OHLC ohlc : ohlcList) {
			if (!ohlc.getNews().isEmpty()) {
				List<GTPNewsDomain> gtpNewsDomains = convertJsonTypeToObject(ohlc);

				gtpNewsDomains.stream()
					.filter(gtpNewsDomain -> gtpNewsDomain.getNext().isPresent())
					.filter(gtpNewsDomain -> gtpNewsDomain.getNext().get().isAfter(baseDate))
					.forEach(news::add);
			}
		}

		return news;
	}

	public List<GTPNewsDomain> getAllNews() {
		List<GTPNewsDomain> news = new ArrayList<>();

		for (OHLC ohlc : ohlcList) {
			if (!ohlc.getNews().isEmpty()) {
				news.addAll(convertJsonTypeToObject(ohlc));
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

	private List<GTPNewsDomain> convertJsonTypeToObject(OHLC ohlc) {
		List<GTPNewsDomain> result = new ArrayList<>();

		if (ohlc.getNews().isEmpty()) {
			log.debug("Cant convert json node to news {} {}", stock.getName(), ohlc.getDate());
			return result;
		}

		ObjectMapper mapper = AppConfig.customObjectMapper();
		return mapper.convertValue(ohlc.getNews(),
			mapper.getTypeFactory().constructCollectionType(List.class, GTPNewsDomain.class));
	}

	private OHLC getSameDateOHLC(LocalDate date) {
		for (OHLC ohlc : ohlcList) {
			if (ohlc.getDate().equals(date)) {
				return ohlc;
			}
		}

		throw new NoResultException("Can't found ohlc data");
	}

	public LocalDate calculateStartDayForUpdateStockChart() {
		if (ohlcList.isEmpty()) {
			return LocalDate.of(1999, 1, 1); // '1999-01-01' 날짜 생성
		}

		LocalDate date = ohlcList.getLast().getDate();
		return date.plusDays(1);
	}

	public void mergeStockChart(StockChart stockChart) {
		Map<LocalDate, OHLC> ohlcMap = ohlcList.stream()
			.collect(Collectors.toMap(OHLC::getDate, Function.identity()));

		for (OHLC ohlc : stockChart.getOhlcList()) {
			OHLC ohlcFromMap = ohlcMap.get(ohlc.getDate());
			if (ohlcFromMap != null) {
				ohlc.addChart(stockChart);
				ohlcList.add(ohlcFromMap);
			}
		}
	}

	public void setStock(Stock stock) {
		this.stock = stock;
	}

}
