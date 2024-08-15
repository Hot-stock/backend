package com.bjcareer.search.retrieval.noSQL;

import java.util.List;

import org.bson.Document;
import org.bson.types.ObjectId;

import com.bjcareer.search.repository.noSQL.DocumentRepository;
import com.bjcareer.search.retrieval.Trie;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class DocumentTrieService implements Trie {
	private final DocumentRepository repository;

	@Override
	public void insert(String keyword, Long searchCount){
		String query = "";
		ObjectId parendId = null;
		Document lastDocument = null;

		for (int i = 0; i < keyword.length(); i++) {
			query += keyword.charAt(i);

			Document parentNode = repository.findSingleByKeyword(DocumentQueryKeywords.KEYWORD, query);

			if(parentNode != null){
				parendId = parentNode.getObjectId(DocumentQueryKeywords.KEY);
			}else{
				lastDocument = changeNodeToDocument(query, parendId);
				parendId = repository.saveDocument(keyword, lastDocument);
			}
		}

		if (lastDocument == null) {
			return;
		}

		repository.updateKeyword(keyword, DocumentQueryKeywords.SEARCH_COUNT, parendId, searchCount);
		repository.updateKeyword(keyword, DocumentQueryKeywords.END_OF_WORD, parendId, true);

		updateParentToChild(keyword, lastDocument, parendId);
	}

	public List<String> search(String keyword){
		Document rootDocument = repository.findSingleByKeyword(DocumentQueryKeywords.KEYWORD, keyword);
		return repository.getkeyworkList(keyword,rootDocument);
	}


	public void updateParentToChild(String keyword, Document endOfDocument, ObjectId childId) {
		ObjectId parentId = endOfDocument.getObjectId(DocumentQueryKeywords.PARENT_ID);

		while(parentId != null){
			repository.setChildIdToParentDocument(keyword, childId, parentId);
			parentId = repository.findByObjectId(keyword, parentId).getObjectId(DocumentQueryKeywords.PARENT_ID);
		}
	}

	private Document changeNodeToDocument(String query, ObjectId parendId) {
		Document document = new Document();

		document.put(DocumentQueryKeywords.KEYWORD, query);
		document.put(DocumentQueryKeywords.PARENT_ID, parendId);
		document.put(DocumentQueryKeywords.SEARCH_COUNT, 0);
		document.put(DocumentQueryKeywords.END_OF_WORD, false);

		return document;
	}

}
