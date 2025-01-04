package com.bjcareer.search.out.persistence.noSQL;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.springframework.stereotype.Repository;

import com.bjcareer.search.domain.News;
import com.bjcareer.search.domain.PaginationDomain;
import com.bjcareer.search.domain.gpt.thema.GPTThemaNewsDomain;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Sorts;

import lombok.extern.slf4j.Slf4j;

@Repository
@Slf4j
public class DocumentAnalyzeThemaRepository {
	private final MongoCollection<Document> collection;
	private static final String DATABASE_NAME = "news";
	public static final String COLLECTION_NAME = "thema-news";

	public DocumentAnalyzeThemaRepository(MongoClient mongoClient) {
		collection = mongoClient.getDatabase(DATABASE_NAME).getCollection(COLLECTION_NAME);
	}

	public List<GPTThemaNewsDomain> getThemaNews(String themaName) {
		Bson filter = Filters.and(
			Filters.eq("isRelatedThema", true),
			Filters.eq("themaInfo.name", themaName)
		);

		long totalCount = collection.countDocuments(filter);
		List<Document> documents = collection.find(filter)
			.sort(Sorts.ascending("news.pubDate"))
			.into(new ArrayList<>());

		List<GPTThemaNewsDomain> result = documents.stream()
			.map(t -> convertDocumentsToGPTNewsDomains(t, themaName))
			.toList();

		return result;
	}

	public PaginationDomain<GPTThemaNewsDomain> getThemaNewsWithPagination(int page, int size, String themaName) {
		Bson filter = Filters.and(
			Filters.eq("isRelatedThema", true),
			Filters.eq("themaInfo.name", themaName)
		);

		long totalCount = collection.countDocuments(filter);
		List<Document> documents = collection.find(filter)
			.skip((page - 1) * size)
			.limit(size)
			.sort(Sorts.ascending("next"))
			.into(new ArrayList<>());

		List<GPTThemaNewsDomain> result = documents.stream()
			.map(t -> convertDocumentsToGPTNewsDomains(t, themaName))
			.toList();

		return new PaginationDomain<>(result, totalCount, page, size);
	}

	private GPTThemaNewsDomain convertDocumentsToGPTNewsDomains(Document document, String themaName) {
		String summary = document.getString("summary");
		String upComingDate = getUpComingDate(document);
		String upComingDateReason = document.getString("upcomingDateReasonFact");
		News news = changeDocumentToNewsDomain(document);

		return new GPTThemaNewsDomain(themaName, summary, upComingDate, upComingDateReason, news);
	}

	private News changeDocumentToNewsDomain(Document document) {
		Document newsDocument = document.get("news", Document.class);

		String title = newsDocument.getString("title");
		String newsLink = newsDocument.getString("newsLink");
		String imgLink = newsDocument.get("imgLink", String.class);
		String description = newsDocument.getString("description");
		LocalDate pubDate = newsDocument.getDate("pubDate").toInstant().atOffset(ZoneOffset.UTC).toLocalDate();
		String content = newsDocument.getString("content");

		return new News(title, newsLink, imgLink, description, pubDate, content);
	}

	private String getUpComingDate(Document document) {
		if (document.getDate("upcomingDate") != null) {
			return document.getDate("upcomingDate").toInstant().atOffset(ZoneOffset.UTC).toLocalDate().toString();
		}
		return "";
	}
}
