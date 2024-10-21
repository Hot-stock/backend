package com.bjcareer.stockservice.timeDeal.domain.coupon;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class OutboxCoupon {
	@Id
	@GeneratedValue
	private Long id;

	@JdbcTypeCode(SqlTypes.JSON)
	@Column(columnDefinition = "json")
	private String payload; //json
	private boolean isDelivered;

	public OutboxCoupon(String payload, boolean isDelivered) {
		this.payload = payload;
		this.isDelivered = isDelivered;
	}
}
