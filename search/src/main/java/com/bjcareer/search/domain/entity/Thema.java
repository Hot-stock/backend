package com.bjcareer.search.domain.entity;

import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.annotations.BatchSize;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
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
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "thema", uniqueConstraints = {
	@UniqueConstraint(columnNames = {"stock_id", "thema_info_id"})  // 복합 유니크 키 설정
})
public class Thema {
	@Id
	@GeneratedValue
	private Long id;

	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "STOCK_ID")
	private Stock stock;

	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "THEMA_INFO_ID")
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

	@Override
	public String toString() {
		return "Thema{" +
			"id=" + id +
			", stock=" + stock +
			", themaInfo=" + themaInfo +
			", updatedAt=" + updatedAt +
			'}';
	}
}
