package com.bjcareer.search.domain.entity;

import java.time.LocalDate;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import com.bjcareer.search.config.AppConfig;
import com.bjcareer.search.domain.gpt.GPTStockNewsDomain;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(
	uniqueConstraints = @UniqueConstraint(columnNames = {"stock_chart_id", "date"})
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class OHLC {
	@Id
	@GeneratedValue
	private Long id;

	private int open;
	private int high;
	private int low;
	private int close;

	private Long volume;
	private int percentageIncrease;

	@ManyToOne
	@JoinColumn(name = "stock_chart_id")
	private StockChart chart;

	private LocalDate date;

	@JdbcTypeCode(SqlTypes.JSON)
	private ArrayNode news = JsonNodeFactory.instance.arrayNode();

	public OHLC(int open, int high, int low, int close, int percentageIncrease, Long volume, LocalDate date) {
		this.open = open;
		this.high = high;
		this.low = low;
		this.close = close;
		this.date = date;
		this.volume = volume;
		this.percentageIncrease = percentageIncrease;
	}

	public void addRoseNews(GPTStockNewsDomain news) {
		this.news.add(AppConfig.customObjectMapper().convertValue(news, JsonNode.class));
	}

	public void addChart(StockChart chart) {
		this.chart = chart;
	}
}
