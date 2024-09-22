package com.bjcareer.search.retrieval;

import java.util.List;

public interface Trie {
	void insert(String str, Long searchCount);

	List<String> search(String query);
}
