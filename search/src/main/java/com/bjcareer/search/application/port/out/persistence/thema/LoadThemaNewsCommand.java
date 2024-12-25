package com.bjcareer.search.application.port.out.persistence.thema;

import java.time.LocalDate;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class LoadThemaNewsCommand {
	private final String themaName;
	private final LocalDate date;

	private int page;
	private int size;

	public LoadThemaNewsCommand(String themaName, int page, int size) {
		this.themaName = themaName;
		this.page = page;
		this.size = size;
		this.date = LocalDate.now();
	}
}
