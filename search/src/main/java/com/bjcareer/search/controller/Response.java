package com.bjcareer.search.controller;

import org.springframework.http.HttpStatus;

import lombok.Data;


@Data
public class Response <T>{
	private HttpStatus status;
	private String message;
	private T response;

	public Response(HttpStatus status, String message, T data) {
		this.status = status;
		this.message = message;
		this.response = data;
	}
}
