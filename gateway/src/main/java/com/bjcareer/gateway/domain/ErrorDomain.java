package com.bjcareer.gateway.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ErrorDomain {
	private String message;

	public ErrorDomain(String message) {
		this.message = message;
	}

	public ErrorDomain() {
	}
}
