package com.bjcareer.payment.adapter.in.response;

import org.springframework.http.HttpStatus;

import lombok.Data;

@Data
public class ApiResponse<T> {
	private int statusCode;
	private String message;
	private T data;

	public ApiResponse(HttpStatus statusCode, String message, T data) {
		this.statusCode = statusCode.value();
		this.message = message;
		this.data = data;
	}
}