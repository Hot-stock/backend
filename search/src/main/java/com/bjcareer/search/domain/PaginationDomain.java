package com.bjcareer.search.domain;

import java.util.List;

import lombok.Getter;

@Getter
public class PaginationDomain<T> {
	private final List<T> content;     // 현재 페이지 데이터
	private final long totalElements; // 전체 데이터 개수
	private final int currentPage;    // 현재 페이지 번호
	private final int pageSize;       // 페이지 크기

	public PaginationDomain(List<T> content, long totalElements, int currentPage, int pageSize) {
		this.content = content;
		this.totalElements = totalElements;
		this.currentPage = currentPage;
		this.pageSize = pageSize;
	}
}
