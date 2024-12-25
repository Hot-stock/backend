package com.bjcareer.search.in.api.controller.dto;

import java.util.List;

import lombok.Getter;

@Getter
public class PageResponseDTO<T> {
	private List<T> content;     // 페이지 데이터
	private long totalElements; // 전체 데이터 개수
	private int totalPages;     // 전체 페이지 수
	private int currentPage;    // 현재 페이지 번호
	private int pageSize;       // 페이지 크기

	public PageResponseDTO(List<T> content, long totalElements, int currentPage, int pageSize) {
		this.content = content;
		this.totalElements = totalElements;
		this.currentPage = currentPage;
		this.pageSize = pageSize;
		this.totalPages = (int)Math.ceil((double)totalElements / pageSize);
	}
}


