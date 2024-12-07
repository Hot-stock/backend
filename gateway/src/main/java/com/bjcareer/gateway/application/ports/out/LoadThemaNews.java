package com.bjcareer.gateway.application.ports.out;

import java.time.LocalDate;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class LoadThemaNews {
	private final String code;
	private final String name;
	private final LocalDate date;
}
