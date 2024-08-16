package com.bjcareer.stockservice.timeDeal.domain.user;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class User {
	@Id
	@GeneratedValue
	@Column(name = "user_id")
	private Long id;
	private String userPK; //pk가 user임
	private Long unUsedCouponNum;
}
