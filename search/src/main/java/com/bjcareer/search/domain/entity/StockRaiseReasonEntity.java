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
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class StockRaiseReasonEntity {
	@Id
	@GeneratedValue
	@Column(name = "STOCK_RAISE_REASON_ID")
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "STOCK_ID")
	private Stock stock;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "THEMA_INFO_ID")
	private ThemaInfo themaInfo;

	//상승이유
	private String reason;

	//뉴스 링크
	private String newsLink;

	//다음 상승 이유
	private String nextReason;

	//다음 이벤트 시작일
	private LocalDate next;
	//뉴스 발행일
	private LocalDate newsPubDate;

	public StockRaiseReasonEntity(Stock stock, ThemaInfo themaInfo, String reason, String newsLink, String nextReason,
		LocalDate next, LocalDate newsPubDate) {
		this.stock = stock;
		this.themaInfo = themaInfo;
		this.reason = reason;
		this.newsLink = newsLink;
		this.nextReason = nextReason;
		this.next = next;
		this.newsPubDate = newsPubDate;
	}

	public StockRaiseReasonEntity(String reason, String newsLink, String nextReason,
		LocalDate next, LocalDate localDate) {
		this(null, null, reason, newsLink, nextReason, next, localDate);
	}
}
