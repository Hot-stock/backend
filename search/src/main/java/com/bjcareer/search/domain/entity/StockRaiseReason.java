package com.bjcareer.search.domain.entity;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StockRaiseReasonEntity {
	@Id
	@GeneratedValue
	@Column(name = "STOCK_RAISE_REASON")
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "STOCK_ID")
	private Stock stock;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "THEMA_ID")
	private Thema thema;

	//상승이유
	private String reason;

	//다음 시작일
	private LocalDate next;

	//뉴스 링크
	private String newsLink;

	//뉴스 발행일
	private LocalDate localDate;

	public StockRaiseReason(Stock stock, Thema thema, String reason, LocalDate next, String newsLink,
		LocalDate localDate) {
		this.stock = stock;
		this.thema = thema;
		this.reason = reason;
		this.next = next;
		this.newsLink = newsLink;
		this.localDate = localDate;
	}
}
