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
	public void insert(String str, Long searchCount){
		String query = "";
		ObjectId parendId = null;
		Document lastDocument = null;

		for (int i = 0; i < str.length(); i++) {
			query += str.charAt(i);

			Document parentNode = repository.findSingleByKeyword(DocumentQueryKeywords.KEYWORD, query);

			if(parentNode != null){
				parendId = parentNode.getObjectId(DocumentQueryKeywords.KEY);
			}else{
				lastDocument = changeNodeToDocument(query, parendId);
				parendId = repository.saveDocument(lastDocument);
			}
		}

		if (lastDocument == null) {
			return;
		}

		repository.updateKeyword(DocumentQueryKeywords.SEARCH_COUNT, parendId, searchCount);
		repository.updateKeyword(DocumentQueryKeywords.END_OF_WORD, parendId, true);

		updateParentToChild(lastDocument, parendId);
	}

	public List<String> search(String query){
		Document rootDocument = repository.findSingleByKeyword(DocumentQueryKeywords.KEYWORD, query);
		return repository.getkeyworkList(rootDocument);
	}


	public void updateParentToChild(Document endOfDocument, ObjectId childId) {
		ObjectId parentId = endOfDocument.getObjectId(DocumentQueryKeywords.PARENT_ID);

		while(parentId != null){
			repository.setChildIdToParentDocument(childId, parentId);
			parentId = repository.findByObjectId(parentId).getObjectId(DocumentQueryKeywords.PARENT_ID);
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
