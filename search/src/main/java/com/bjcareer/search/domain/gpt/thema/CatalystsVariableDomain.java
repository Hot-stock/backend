package com.bjcareer.search.domain.gpt.thema;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CatalystsVariableDomain {
	private String keywrod;
	private String catalyst;

	public CatalystsVariableDomain(String keywrod, String catalyst) {
		this.keywrod = keywrod;
		this.catalyst = catalyst;
	}
}
