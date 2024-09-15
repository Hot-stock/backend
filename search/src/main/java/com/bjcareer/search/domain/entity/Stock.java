package com.bjcareer.search.domain.entity;


import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.BatchSize;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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

	@OneToMany(mappedBy = "stock")
	@BatchSize(size = 10)
	List<Thema> themas = new ArrayList<>();

	public Stock(String code, String name, Market market, String href, Long issuedShares, Long price) {
		this.code = code;
		this.name = name;
		this.market = market;
		this.href = href;
		this.issuedShares = issuedShares;
		this.price = price;
		this.marketCapitalization = issuedShares * price;
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
}
