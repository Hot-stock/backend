package com.bjcareer.gateway.out.api.search.response;

import java.util.List;

import lombok.Getter;

@Getter
public class PageResponseDTO<T> {
	private List<T> content;     // 페이지 데이터
	private long totalElements; // 전체 데이터 개수
	private int totalPages;     // 전체 페이지 수
	private int currentPage;    // 현재 페이지 번호
	private int pageSize;       // 페이지 크기
}

