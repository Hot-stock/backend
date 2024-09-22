package com.bjcareer.search.repository.noSQL;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.bson.Document;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.InvalidDataAccessApiUsageException;

import com.bjcareer.search.candidate.noSQL.DocumentQueryKeywords;

@SpringBootTest
class DocumentRepositoryTest {

	private static final String TEST_KEYWORD = "test_keyword";
	private static final int SEARCH_COUNT = 0;
	private static final boolean END_OF_WORD = false;

	@Autowired
	private DocumentRepository documentRepository;

	private Document document;
	private ObjectId documentId;

	@BeforeEach
	void setUp() {
		document = createTestDocument(TEST_KEYWORD, SEARCH_COUNT, END_OF_WORD);
		documentId = documentRepository.saveDocument(document);
	}

	@AfterEach
	void tearDown() {
		if (documentId != null) {
			documentRepository.deleteOne(documentId);
		}
	}

	@Test
	void testInsertAndUpdateKeyword() {
		documentRepository.insertAndUpdateKeyword(TEST_KEYWORD);

		for (int i = 0; i < TEST_KEYWORD.length(); i++) {
			Document findResult = documentRepository.findSingleByKeyword(TEST_KEYWORD.substring(0, i + 1));
			assertEquals(TEST_KEYWORD.substring(0, i + 1), findResult.getString(DocumentQueryKeywords.KEYWORD));
		}

		Document byKeyword = documentRepository.findSingleByKeyword(TEST_KEYWORD);
		assertEquals(1, byKeyword.getInteger(DocumentQueryKeywords.SEARCH_COUNT));

		// 추가: 테스트 후 데이터 삭제
		ObjectId insertedDocumentId = byKeyword.getObjectId(DocumentQueryKeywords.KEY);
		documentRepository.deleteOne(insertedDocumentId);
	}

	@Test
	void updateParentToChild() {
		ObjectId parentId = documentRepository.saveDocument(document);

		Document childDocument = createTestDocument("child_keyword", parentId, SEARCH_COUNT, END_OF_WORD);
		ObjectId childId = documentRepository.saveDocument(childDocument);

		documentRepository.updateChildToParent(childDocument, childId);

		Document parent = documentRepository.findByObjectId(parentId);
		List<ObjectId> children = parent.getList(DocumentQueryKeywords.CHILDS, ObjectId.class);
		assertTrue(children.contains(childId));

		// 추가: 테스트 후 데이터 삭제
		documentRepository.deleteOne(childId);
		documentRepository.deleteOne(parentId);
	}

	@Test
	void findSingleByKeyword() {
		Document singleByKeyword = documentRepository.findSingleByKeyword(TEST_KEYWORD);
		assertEquals(document, singleByKeyword);

		// 추가: 테스트 후 데이터 삭제
		ObjectId foundId = singleByKeyword.getObjectId(DocumentQueryKeywords.KEY);
		documentRepository.deleteOne(foundId);
	}

	@Test
	void saveDocument() {
		ObjectId objectId = documentRepository.saveDocument(document);
		Document byObjectId = documentRepository.findByObjectId(objectId);
		assertEquals(document, byObjectId);

		// 추가: 테스트 후 데이터 삭제
		documentRepository.deleteOne(objectId);
	}

	@Test
	void updateKeyword() {
		documentRepository.setEndOfWord(documentId, true);
		Document byObjectId = documentRepository.findByObjectId(documentId);
		assertTrue(byObjectId.getBoolean(DocumentQueryKeywords.END_OF_WORD));

		// 데이터 삭제는 @AfterEach의 tearDown에서 처리됨
	}

	@Test
	void updateSearchCountIfNotExist() {
		assertThrows(InvalidDataAccessApiUsageException.class,
			() -> documentRepository.updateSearchCount(TEST_KEYWORD));
	}

	@Test
	void findAll() {
		// given
		Document secondDocument = createTestDocument("another_keyword", SEARCH_COUNT, END_OF_WORD);
		ObjectId secondDocumentId = documentRepository.saveDocument(secondDocument);

		// when
		List<Document> allDocuments = documentRepository.findAll();

		// then
		assertNotNull(allDocuments);
		assertTrue(allDocuments.size() > 0);

		// 추가: 저장된 모든 데이터 삭제
		documentRepository.deleteOne(secondDocumentId);
	}

	// Helper method to create a test document
	private Document createTestDocument(String keyword, int searchCount, boolean endOfWord) {
		Document document = new Document();
		document.put(DocumentQueryKeywords.KEYWORD, keyword);
		document.put(DocumentQueryKeywords.SEARCH_COUNT, searchCount);
		document.put(DocumentQueryKeywords.END_OF_WORD, endOfWord);
		return document;
	}

	// Overloaded method to handle parentId case
	private Document createTestDocument(String keyword, ObjectId parentId, int searchCount, boolean endOfWord) {
		Document document = createTestDocument(keyword, searchCount, endOfWord);
		document.put(DocumentQueryKeywords.PARENT_ID, parentId);
		return document;
	}
}
