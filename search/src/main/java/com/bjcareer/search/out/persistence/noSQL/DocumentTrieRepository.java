package com.bjcareer.search.out.persistence.noSQL;

import static com.mongodb.client.model.Filters.*;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.stereotype.Repository;

import com.bjcareer.search.candidate.noSQL.DocumentQueryKeywords;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Updates;

import lombok.extern.slf4j.Slf4j;

@Repository
@Slf4j
public class DocumentTrieRepository {
	private final MongoCollection<Document> collection;
	public static final String TRIE_COLLECTION_NAME = "Trie";

	public DocumentTrieRepository(MongoDatabase database) {
		this.collection = database.getCollection(TRIE_COLLECTION_NAME);
	}

	public void insertAndUpdateKeyword(String keyword) {
		StringBuilder query = new StringBuilder();
		ObjectId parendId = null;
		Document lastDocument = null;

		for (int i = 0; i < keyword.length(); i++) {
			query.append(keyword.charAt(i));

			Document parentNode = findSingleByKeyword(query.toString());

			if (parentNode != null) {
				parendId = parentNode.getObjectId(DocumentQueryKeywords.KEY);
			} else {
				lastDocument = changeNodeToDocument(query.toString(), parendId);
				parendId = saveDocument(lastDocument);
			}
		}

		if (lastDocument == null) {
			return;
		}

		updateSearchCount(keyword);
		setEndOfWord(parendId, true);
		updateChildToParent(lastDocument, parendId);
	}

	public void updateChildToParent(Document endOfDocument, ObjectId childId) {
		ObjectId parentId = endOfDocument.getObjectId(DocumentQueryKeywords.PARENT_ID);

		while (parentId != null) {
			setChildIdToParentDocument(childId, parentId);
			parentId = findByObjectId(parentId).getObjectId(DocumentQueryKeywords.PARENT_ID);
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

	public Document findSingleByKeyword(String query) {
		return collection.find(eq(DocumentQueryKeywords.KEYWORD, query)).first();
	}

	public Document findByObjectId(ObjectId objectId) {
		return collection.find(eq(DocumentQueryKeywords.KEY, objectId)).first();
	}

	public ObjectId saveDocument(Document document) {
		collection.insertOne(document);
		return document.getObjectId(DocumentQueryKeywords.KEY);
	}

	public void setChildIdToParentDocument(ObjectId childId, ObjectId parentId) {
		collection.updateOne(
			eq(DocumentQueryKeywords.KEY, parentId),
			Updates.addToSet("childs", childId)
		);
	}

	public void setEndOfWord(ObjectId id, Boolean entOfWord) {
		collection.updateOne(eq(DocumentQueryKeywords.KEY, id),
			Updates.set(DocumentQueryKeywords.END_OF_WORD, entOfWord));
	}

	public void updateSearchCount(String keyword){
		if (findSingleByKeyword(keyword) == null) {
			log.error("keyword not found");
			throw new InvalidDataAccessApiUsageException("keyword not found");
		}
		collection.updateOne(eq(DocumentQueryKeywords.KEYWORD, keyword),
			Updates.inc(DocumentQueryKeywords.SEARCH_COUNT, 1));
	}

	public List<String> getkeyworkList(Document rootDocument) {
		List<String> result = new ArrayList<>();

		if (rootDocument == null) {
			return result;
		}

		List<ObjectId> childs = rootDocument.getList(DocumentQueryKeywords.CHILDS, ObjectId.class);

		if (childs != null) {
			childs.forEach(
				c -> result.add(findByObjectId(c).get(DocumentQueryKeywords.KEYWORD, String.class)));
		}

		return result;
	}

	public List<Document> findAll() {
		List<Document> documents = new ArrayList<>();
		collection.find().iterator().forEachRemaining(documents::add);
		return documents;
	}

	public void deleteOne(ObjectId objectId) {
		collection.deleteOne(eq(DocumentQueryKeywords.KEY, objectId));
	}
}
