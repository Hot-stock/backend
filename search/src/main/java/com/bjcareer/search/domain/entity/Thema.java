package com.bjcareer.search.domain.entity;

import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.CascadeType;
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
import lombok.ToString;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString
@Table(name = "thema", uniqueConstraints = {
	@UniqueConstraint(columnNames = {"stock_id", "thema_id"})  // 복합 유니크 키 설정
})
public class Thema {
	@Id
	@GeneratedValue
	private Long id;

	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "STOCK_ID")
	private Stock stock;

	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "THEMA_ID")
	private ThemaInfo themaInfo;

	private LocalDateTime updatedAt;

	public Thema(Stock stock, ThemaInfo themaInfo) {
		this.stock = stock;
		this.themaInfo = themaInfo;
		this.updatedAt = LocalDateTime.now();
	}

	public String getKey(){
		return UUID.nameUUIDFromBytes((stock.getCode() + themaInfo.getName()).getBytes()).toString();
	}
}
