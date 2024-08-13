package com.bjcareer.search.retrieval.cache;

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
