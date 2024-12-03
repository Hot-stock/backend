package com.bjcareer.search.application.port.out.persistence.thema;

import org.springframework.data.domain.Pageable;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class LoadThemaUsingkeywordCommand {
	private final String keyword;
}
