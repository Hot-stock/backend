package com.bjcareer.search.domain.entity;

import java.time.LocalDate;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import com.bjcareer.search.domain.News;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString
public class OHLC {
	@Id
	private Long id;

	private int open;
	private int high;
	private int low;
	private int close;

	private int percentageIncrease;

	@ManyToOne
	private StockChart chart;

	@JdbcTypeCode(SqlTypes.JSON)
	private JsonNode news;

	@Column(unique = true)
	private LocalDate date;

	public OHLC(int open, int high, int low, int close, LocalDate date) {
		this.open = open;
		this.high = high;
		this.low = low;
		this.close = close;
		this.date = date;

		this.percentageIncrease = calculatePercentageIncrease();
	}

	private int calculatePercentageIncrease() {
		return (int)Math.ceil((double)(high - open) / open * 100);
	}

	public void addRoseNews(News news) {
		ObjectMapper mapper = new ObjectMapper();
		this.news = mapper.convertValue(news, JsonNode.class);
	}
}
