package com.bjcareer.search.out.persistence.noSQL;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.springframework.stereotype.Repository;

import com.bjcareer.search.application.port.out.persistence.stock.LoadStockRaiseReason;
import com.bjcareer.search.application.port.out.persistence.thema.LoadThemaNewsCommand;
import com.bjcareer.search.config.AppConfig;
import com.bjcareer.search.domain.News;
import com.bjcareer.search.domain.PaginationDomain;
import com.bjcareer.search.domain.gpt.GPTStockNewsDomain;
import com.bjcareer.search.domain.gpt.thema.GPTThemaNewsDomain;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Accumulators;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Sorts;

import lombok.extern.slf4j.Slf4j;

@Repository
@Slf4j
public class DocumentAnalyzeRepository {
	private final MongoCollection<Document> stockNewsCollection;
	public static final String COLLECTION_NAME = "news";

	public DocumentAnalyzeRepository(MongoClient mongoClient) {
		stockNewsCollection = mongoClient.getDatabase(COLLECTION_NAME).getCollection(COLLECTION_NAME);
	}

	public PaginationDomain<GPTStockNewsDomain> getUpcomingNews(int page, int size) {
		Bson filter = Filters.and(
			Filters.gt("next", LocalDate.now(AppConfig.ZONE_ID)),
			Filters.ne("stockName", "nil"),
			Filters.ne("stockCode", null)
		);

		// 애그리게이션 파이프라인
		List<Bson> pipeline = Arrays.asList(
			// 필터 조건 적용
			Aggregates.match(filter),

			// 조인 수행
			Aggregates.lookup(
				"stock-thema-news", // 조인할 컬렉션 이름
				"_id",              // stockNewsCollection의 필드
				"_id",          // themaNewsCollection의 필드 (조인 조건)
				"joinedData"        // 조인 결과 배열 이름
			),


			// Aggregates.match(Filters.not(Filters.size("joinedData", 0))),
			// 4. 중복 제거 (next, stockCode 기준)
			Aggregates.group(
				new Document("next", "$next").append("stockCode", "$stockCode"), // 그룹 기준
				Accumulators.first("document", "$$ROOT") // 그룹에서 첫 번째 문서만 선택
			),
			// 정렬
			Aggregates.sort(Sorts.ascending("document.next")),

			// 페이지네이션
			Aggregates.skip((page - 1) * size),
			Aggregates.limit(size)
		);

		// 애그리게이션 실행
		AggregateIterable<Document> results = stockNewsCollection.aggregate(pipeline);
		long totalCount = stockNewsCollection.countDocuments(filter);

		List<Document> documents = new ArrayList<>();
		for (Document doc : results) {
			Document document = doc.get("document", Document.class);
			documents.add(document);
		}

		List<GPTStockNewsDomain> result = convertDocumentsToGPTNewsDomains(documents);
		return new PaginationDomain<>(result, totalCount, page, size);
	}

	public List<GPTStockNewsDomain> getUpcomingNewsByStockName(String stockName) {
		List<Document> documents = stockNewsCollection.find(
			Filters.and(
				Filters.gt("next", LocalDate.now(AppConfig.ZONE_ID)),
				Filters.eq("stockName", stockName)
			)
		).sort(Sorts.ascending("next")).into(new ArrayList<>());

		List<GPTStockNewsDomain> result = convertDocumentsToGPTNewsDomains(documents);

		return result;
	}

	public List<GPTStockNewsDomain> getStockNewsByLinks(List<String> link) {
		ArrayList<Document> documents = stockNewsCollection.find(Filters.in("_id", link)).into(new ArrayList<>());
		return convertDocumentsToGPTNewsDomains(documents);
	}

	public List<GPTStockNewsDomain> getRaiseReason(LoadStockRaiseReason command) {
		Bson filter = Filters.and(
			Filters.eq("stockName", command.getStockName()),
			Filters.eq("isRelated", true)
		);

		if (command.getDate() != null) {
			// KST -> UTC 변환
			ZonedDateTime startKST = command.getDate().atStartOfDay(AppConfig.ZONE_ID);
			ZonedDateTime endKST = command.getDate().atStartOfDay(AppConfig.ZONE_ID).plusDays(1);

			Date startDate = Date.from(startKST.toInstant());
			Date endDate = Date.from(endKST.toInstant());

			filter = Filters.and(
				filter,
				Filters.gte("news.pubDate", startDate),
				Filters.lt("news.pubDate", endDate));
		}

		List<Document> documents = stockNewsCollection.find(filter)
			.sort(Sorts.descending("news.pubDate"))
			.into(new ArrayList<>());

		return convertDocumentsToGPTNewsDomains(documents);
	}

	public List<String> getThemasOfNews(String link, String stockName) {
		List<String> result = new ArrayList<>();
		Bson filter = Filters.and(
			Filters.eq("_id", link)
		);

		// themaNewsCollection.find(filter).forEach(doc -> {
		// 	Document document = doc.get("themaInfo", Document.class);
		// 	List<String> stockNames = (List<String>)document.get("stockName");
		//
		// 	if (stockNames.contains(stockName)) {
		// 		result.add(document.getString("name"));
		// 	}
		// });

		return result;
	}

	public List<GPTThemaNewsDomain> getThemaNews(LoadThemaNewsCommand command) {
		Bson filter = Filters.and(
			Filters.eq("themaInfo.name", command.getThemaName()),
			Filters.eq("isRelated", true)
		);

		if (command.getDate() != null) {
			filter = Filters.and(
				filter,
				Filters.eq("news.pubDate", command.getDate())
			);
		}

		// List<Document> documents = themaNewsCollection.find(filter)
		// 	.sort(Sorts.descending("news.pubDate"))
		// 	.into(new ArrayList<>());

		// return convertDocumentsToGPTThemaDomains(documents, command.getThemaName());

		return new ArrayList<>();
	}

	private List<GPTThemaNewsDomain> convertDocumentsToGPTThemaDomains(List<Document> documents, String themaName) {
		List<GPTThemaNewsDomain> result = new ArrayList<>();

		for (Document document : documents) {
			GPTThemaNewsDomain gptThemaNewsDomain = changeDocumentToGPTThemaDomain(document, themaName);
			result.add(gptThemaNewsDomain);
		}

		return result;
	}

	private GPTThemaNewsDomain changeDocumentToGPTThemaDomain(Document document, String themaName) {
		String summary = document.getString("summary");
		String upComingDate = getUpComingDate(document);
		String upComingDateReason = document.getString("upcomingDateReasonFact");
		News news = changeDocumentToNewsDomain(document);

		return new GPTThemaNewsDomain(themaName, summary, upComingDate, upComingDateReason, news);
	}

	private List<GPTStockNewsDomain> convertDocumentsToGPTNewsDomains(List<Document> documents) {
		List<GPTStockNewsDomain> result = new ArrayList<>();
		for (Document document : documents) {
			GPTStockNewsDomain gptStockNewsDomain = changeDocumentToGPTNewsDomain(document);
			News news = changeDocumentToNewsDomain(document);

			gptStockNewsDomain.addNewsDomain(news);
			result.add(gptStockNewsDomain);
		}

		return result;
	}

	private GPTStockNewsDomain changeDocumentToGPTNewsDomain(Document document) {
		String stockName = document.getString("stockName");
		String reason = document.getString("reason");
		String stockCode = document.getString("stockCode");

		List<String> keywords = (List<String>) document.getOrDefault("keywords", new ArrayList<>());

		String next = getDate(document);

		String nextReason = document.getString("nextReasonFact");
		return new GPTStockNewsDomain(stockName, stockCode, reason, keywords, next.toString(), nextReason);
	}

	private News changeDocumentToNewsDomain(Document document) {
		Document newsDocument = document.get("news", Document.class);


		String title = newsDocument.getString("title");
		String newsLink = newsDocument.getString("newsLink");
		String imgLink = newsDocument.get("imgLink", String.class);
		String description = newsDocument.getString("description");
		LocalDate pubDate = newsDocument.getDate("pubDate").toInstant().atZone(AppConfig.ZONE_ID).toLocalDate();
		String content = newsDocument.getString("content");

		return new News(title, newsLink, imgLink, description, pubDate, content);
	}

	private String getDate(Document document) {
		if (document.getDate("next") != null) {
			return document.getDate("next").toInstant().atZone(AppConfig.ZONE_ID).toLocalDate().toString();
		}
		return "";
	}

	private String getUpComingDate(Document document) {
		if (document.getDate("upcomingDate") != null) {
			return document.getDate("upcomingDate").toInstant().atOffset(ZoneOffset.UTC).toLocalDate().toString();
		}
		return "";
	}
}
