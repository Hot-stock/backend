package com.bjcareer.search.repository.noSQL;

import static com.mongodb.client.model.Filters.*;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Repository;

import com.bjcareer.search.retrieval.noSQL.DocumentQueryKeywords;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Updates;

@Repository
public class DocumentRepository {
	private final MongoCollection<Document> collection;
	public static final String TRIE_COLLECTION_NAME = "Trie";

	public DocumentRepository(MongoDatabase database) {
		this.collection = database.getCollection(TRIE_COLLECTION_NAME);
	}

	public Document findSingleByKeyword(String keyword, String query){
		return collection.find(eq(keyword, query)).first();
	}

	public MongoCursor<Document> findAllByKeyword(String keyword, Object query){
		return collection.find(eq(keyword, query)).cursor();
	}

	public Document findByObjectId(String keyword, ObjectId objectId){
		return collection.find(eq(DocumentQueryKeywords.KEY, objectId)).first();
	}

	public ObjectId saveDocument(String keyword, Document document){
		collection.insertOne(document);
		return document.getObjectId(DocumentQueryKeywords.KEY);
	}

	public void setChildIdToParentDocument(String keyword, ObjectId childId, ObjectId parentId){
		collection.updateOne(
			eq(DocumentQueryKeywords.KEY, parentId),
			Updates.addToSet("childs", childId)
		);
	}

	public void updateKeyword(String keyword, String key, ObjectId id, Object object){
		collection.updateOne(eq(DocumentQueryKeywords.KEY, id), Updates.set(key, object));
	}

	public void updateSearchCount(String keyword){
		collection.updateOne(eq(DocumentQueryKeywords.KEYWORD, keyword), Updates.inc(DocumentQueryKeywords.KEYWORD, 1));
	}

	public List<String> getkeyworkList(String keyword, Document rootDocument){
		List<String> result = new ArrayList<>();

		if (rootDocument == null) {
			return result;
		}

		List<ObjectId> childs = rootDocument.getList(DocumentQueryKeywords.CHILDS, ObjectId.class);

		if (childs != null) {
			childs.forEach(
				c -> result.add(findByObjectId(keyword, c).get(DocumentQueryKeywords.KEYWORD, String.class)));
		}

		return result;
	}
}
