package com.bjcareer.search.application.port.out.persistence.thema;

import java.time.LocalDate;
import java.util.List;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class LoadThemaNewsCommand {
	private final String themaName;
	private final LocalDate date;
}
