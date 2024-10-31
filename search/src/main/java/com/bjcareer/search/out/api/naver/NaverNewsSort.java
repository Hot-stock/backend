package com.bjcareer.search.out.api.naver;

public enum NaverNewsSort {
	SIM("sim"),   // 정확도 기반으로 검색
	DATE("date"); // 최신 기사순으로 검색

	private final String value;

	NaverNewsSort(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}
}
