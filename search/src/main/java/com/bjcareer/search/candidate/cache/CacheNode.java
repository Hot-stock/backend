package com.bjcareer.search.candidate.cache;

import java.util.List;

import lombok.Getter;

@Getter
public class CacheNode {
	private List<String> child;
	private String keyword;

	public CacheNode(String keyword, List<String> child) {
		this.keyword = keyword;
		this.child = child;
	}
}
