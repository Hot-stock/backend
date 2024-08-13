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
	private MongoCollection<Document> collection;
	public static final String TRIE_COLLECTION = "Trie";

	public DocumentRepository(MongoDatabase database) {
		collection = database.getCollection(TRIE_COLLECTION);
	}


	public Document findSingleByKeyword(String keyword, Object query){
		return collection.find(eq(keyword, query)).first();
	}

	public MongoCursor<Document> findAllByKeyword(String keyword, Object query){
		return collection.find(eq(keyword, query)).cursor();
	}

	public Document findByObjectId(ObjectId objectId){
		return collection.find(eq(DocumentQueryKeywords.KEY, objectId)).first();
	}

	public ObjectId saveDocument(Document document){
		collection.insertOne(document);
		return document.getObjectId(DocumentQueryKeywords.KEY);
	}

	public void setChildIdToParentDocument(ObjectId childId, ObjectId parentId){
		collection.updateOne(
			eq(DocumentQueryKeywords.KEY, parentId),
			Updates.addToSet("childs", childId)
		);
	}

	public void updateKeyword(String key, ObjectId id, Object object){
		collection.updateOne(eq(DocumentQueryKeywords.KEY, id), Updates.set(key, object));
	}

	public List<String> getkeyworkList(Document rootDocument){
		List<String> result = new ArrayList<>();

		if (rootDocument == null) {
			return result;
		}

		List<ObjectId> childs = rootDocument.getList(DocumentQueryKeywords.CHILDS, ObjectId.class);

		if (childs != null) {
			childs.stream().forEach(c -> result.add(findByObjectId(c).get(DocumentQueryKeywords.KEYWORD, String.class)));
		}

		return result;
	}
}
