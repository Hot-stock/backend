package com.bjcareer.search.domain.entity;


import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.BatchSize;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
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

	private Long issuedShares;
	private Long price;
	private Long marketCapitalization;

	@OneToMany(mappedBy = "stock", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@BatchSize(size = 10)
	List<Thema> themas = new ArrayList<>();

	@OneToMany(mappedBy = "stock", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@BatchSize(size = 10)
	List<StockRaiseReasonEntity> raiseReasons = new ArrayList<>();

	@OneToMany(mappedBy = "stock", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@BatchSize(size = 100)
	List<OHLC> ohlcList = new ArrayList<>();

	public Stock(String code, String name, Market market, String href, Long issuedShares, Long price) {
		this.code = code;
		this.name = name;
		this.market = market;
		this.href = href;
		this.issuedShares = issuedShares;
		this.price = price;

		if (issuedShares != null && price != null) {
			this.marketCapitalization = issuedShares * price;
		}else {
			this.marketCapitalization = 0L;
		}
	}

	public Stock(String code, String name) {
		this(code, name, null, null, null, null);
	}

	@Override
	public String toString() {
		return "Stock{" +
			"id=" + id +
			", code='" + code + '\'' +
			", name='" + name + '\'' +
			", market=" + market +
			", href='" + href + '\'' +
			", issuedShares=" + issuedShares +
			", price=" + price +
			", marketCapitalization=" + marketCapitalization +
			'}';
	}

	public boolean validStock() {
		return issuedShares != null && price != null;
	}
}
