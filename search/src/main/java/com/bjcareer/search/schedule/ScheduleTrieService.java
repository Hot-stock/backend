package com.bjcareer.search.schedule;

import java.util.List;

import org.bson.Document;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.bjcareer.search.candidate.Trie;
import com.bjcareer.search.candidate.noSQL.DocumentQueryKeywords;
import com.bjcareer.search.out.persistence.repository.noSQL.DocumentRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class ScheduleTrieService {
	private final Trie trie;
	private final DocumentRepository documentRepository;

	// @Scheduled(fixedDelay = 10000)
	//프로메테우스로 빼기
	public void makeTrie() {
		log.debug("TrieService started");
		List<Document> documents = documentRepository.findAll();

		for (Document document : documents) {
			trie.update(document.getString(DocumentQueryKeywords.KEYWORD));
		}

		log.debug("TrieService finished");
	}
}
