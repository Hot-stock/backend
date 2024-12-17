package com.bjcareer.search.domain.entity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.hibernate.annotations.BatchSize;

import com.bjcareer.search.config.AppConfig;
import com.bjcareer.search.domain.gpt.GPTStockNewsDomain;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.NoResultException;
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

	@Transient
	private Stock stock;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "chart", cascade = CascadeType.ALL)
	@BatchSize(size = 100)
	@OrderBy("date ASC")
	private List<OHLC> ohlcList = new ArrayList<>();

	public StockChart(String stockCode, List<OHLC> ohlcList) {
		this.stockCode = stockCode;
		addOHLC(ohlcList);
	}

	public List<GPTStockNewsDomain> loadNewByDate(LocalDate date) {
		OHLC ohlc = getSameDateOHLC(date);
		return convertJsonTypeToObject(ohlc);
	}

	public List<GPTStockNewsDomain> getNextSchedule(LocalDate baseDate) {
		List<GPTStockNewsDomain> news = new ArrayList<>();

		for (OHLC ohlc : ohlcList) {
			if (!ohlc.getNews().isEmpty()) {
				List<GPTStockNewsDomain> GPTStockNewsDomains = convertJsonTypeToObject(ohlc);

				GPTStockNewsDomains.stream()
					.filter(gtpNewsDomain -> gtpNewsDomain.getNext().isPresent())
					.filter(gtpNewsDomain -> gtpNewsDomain.getNext().get().isAfter(baseDate))
					.forEach(news::add);
			}
		}

		return news;
	}

	public List<GPTStockNewsDomain> getAllNews() {
		List<GPTStockNewsDomain> news = new ArrayList<>();

		for (OHLC ohlc : ohlcList) {
			if (!ohlc.getNews().isEmpty()) {
				news.addAll(convertJsonTypeToObject(ohlc));
			}
		}

		return news;
	}

	public Integer calcMovingAverageOfIncrease(int days) {
		if (ohlcList == null || ohlcList.isEmpty() || days <= 0) {
			log.warn("Invalid input data or days");
			return 0;
		}

		int sum = 0;
		int count = 0;

		// 최근 'days'만큼의 데이터만 순회
		for (int i = ohlcList.size() - 1; i >= 0 && count < days; i--) {
			sum += ohlcList.get(i).getPercentageIncrease();
			count++;
		}

		// 평균을 계산
		if (count == 0) {
			return 0; // 데이터가 부족한 경우
		}

		return sum / count; // 이동평균 값 반환
	}

	public void addNewsToOhlc(GPTStockNewsDomain news, LocalDate date) {
		getSameDateOHLC(date).addRoseNews(news);
	}

	public void addOHLC(List<OHLC> ohlcs) {
		for (OHLC ohlc : ohlcs) {
			ohlc.addChart(this);
			this.ohlcList.add(ohlc);
		}
	}

	private List<GPTStockNewsDomain> convertJsonTypeToObject(OHLC ohlc) {
		List<GPTStockNewsDomain> result = new ArrayList<>();

		if (ohlc.getNews().isEmpty()) {
			log.debug("Cannot convert JSON node to news: stock={}, date={}",
				stock != null ? stock.getName() : this.stockCode,
				ohlc.getDate());
			return result;
		}

		ObjectMapper mapper = AppConfig.customObjectMapper();
		return mapper.convertValue(ohlc.getNews(),
			mapper.getTypeFactory().constructCollectionType(List.class, GPTStockNewsDomain.class));
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

	public void mergeOhlc(StockChart stockChart) {
		Map<LocalDate, OHLC> existingOhlcMap = ohlcList.stream()
			.collect(Collectors.toMap(OHLC::getDate, Function.identity()));

		List<OHLC> newOhlcEntries = new ArrayList<>();
		for (OHLC stockOhlc : stockChart.getOhlcList()) {
			OHLC existingOhlc = existingOhlcMap.get(stockOhlc.getDate());

			if (existingOhlc != null) {
				stockOhlc.addChart(this);
				newOhlcEntries.add(existingOhlc);
			}
		}

		ohlcList.addAll(newOhlcEntries);
	}

	public void setStock(Stock stock) {
		this.stock = stock;
	}
}
