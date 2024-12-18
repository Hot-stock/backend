package com.bjcareer.GPTService.domain;

import java.util.Objects;

import com.bjcareer.GPTService.out.api.python.Market;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Stock {
	@Id
	@GeneratedValue
	@Column(name = "STOCK_ID")
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

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof Stock stock))
			return false;
		return Objects.equals(code, stock.code) && Objects.equals(name, stock.name);
	}

	@Override
	public int hashCode() {
		return Objects.hash(code, name);
	}
}
