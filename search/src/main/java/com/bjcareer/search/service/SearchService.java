package com.bjcareer.search.service;

import java.util.List;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

import com.bjcareer.search.repository.noSQL.DocumentRepository;
import com.bjcareer.search.retrieval.Trie;


@Service
@RequiredArgsConstructor
public class SearchService {
	private final DocumentRepository documentRepository;
	private final Trie trie;

	public List<String> getSuggestionKeyword(String keyword){
		return trie.search(keyword);
	}

	public void updateSearchCount(String keyword){
		documentRepository.updateSearchCount(keyword);
	}

}
