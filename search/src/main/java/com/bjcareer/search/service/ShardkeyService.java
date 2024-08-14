package com.bjcareer.search.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.stereotype.Service;
import com.bjcareer.search.repository.noSQL.DocumentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class ShardkeyService {
	private final DocumentRepository documentRepository;

	public Map<String, Integer> getShardingKey(int databaseNum) {
		if (databaseNum <= 0) {
			log.error("The number of databases should be greater than zero.");
			return new HashMap<>();
		}

		Map<String, Integer> keywordCount = documentRepository.getKeywordCount();
		if (keywordCount.isEmpty()) {
			log.info("No keywords found to distribute across shards.");
			return new HashMap<>();
		}

		// 키워드를 값 기준으로 오름차순 정렬
		List<Map.Entry<String, Integer>> sortedKeywords = keywordCount.entrySet().stream()
			.sorted(Map.Entry.comparingByValue())
			.collect(Collectors.toList());

		List<List<String>> sharedList = initShardList(databaseNum);
		long[] shardSums = new long[databaseNum];

		for (Map.Entry<String, Integer> entry : sortedKeywords) {
			String keyword = entry.getKey();
			Integer keywordValue = entry.getValue();

			// 가장 적은 용량을 가진 샤드를 선택
			int smallestShardIndex = getSmallestShardIndex(shardSums);

			// 해당 샤드에 키워드를 추가하고, 샤드 합계 업데이트
			sharedList.get(smallestShardIndex).add(keyword);
			shardSums[smallestShardIndex] += keywordValue;
		}

		// 키워드와 해당 샤드 인덱스를 맵에 저장
		Map<String, Integer> result = new HashMap<>();
		IntStream.range(0, sharedList.size()).forEach(i -> {
			List<String> shard = sharedList.get(i);
			shard.forEach(j -> result.put(j, i));
		});

		return result;
	}

	private int getSmallestShardIndex(long[] shardSums) {
		int smallestShardIndex = 0;
		for (int i = 1; i < shardSums.length; i++) {
			if (shardSums[i] < shardSums[smallestShardIndex]) {
				smallestShardIndex = i;
			}
		}
		return smallestShardIndex;
	}

	private List<List<String>> initShardList(int databaseNum) {
		List<List<String>> shardList = new ArrayList<>();
		for (int i = 0; i < databaseNum; i++) {
			shardList.add(new ArrayList<>());
		}
		return shardList;
	}
}
