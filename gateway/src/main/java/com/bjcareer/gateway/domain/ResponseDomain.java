package com.bjcareer.gateway.domain;

import org.springframework.http.HttpStatusCode;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@RequiredArgsConstructor
@Getter
@ToString
public class ResponseDomain<T> {
	@JsonIgnore
	private final HttpStatusCode statusCode;
	private final T data;
	private final ErrorDomain error;
}
