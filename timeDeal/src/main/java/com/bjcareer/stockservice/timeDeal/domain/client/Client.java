package com.bjcareer.stockservice.timeDeal.domain.client;

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
public class Client {
	@Id
	@GeneratedValue
	@Column(name = "client_id")
	private Long id;
	private String clientPK; //pk가 user임

	public Client(String clientPK) {
		this.clientPK = clientPK;
	}
}
