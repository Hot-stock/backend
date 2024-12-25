package com.bjcareer.search.application.port.out.persistence.thema;

import java.time.LocalDate;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class LoadThemaNewsCommand {
	private final String themaName;
	private final LocalDate date;

	private long id;
	private int page;
	private int size;

	public LoadThemaNewsCommand(long id, int page, int size) {
		this.id = id;
		this.page = page;
		this.size = size;
		this.date = LocalDate.now();
		this.themaName = null;
	}
}
