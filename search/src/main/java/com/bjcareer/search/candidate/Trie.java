package com.bjcareer.search.candidate;

import java.util.List;

public interface Trie {
	void update(String str);

	List<String> search(String query);
}
