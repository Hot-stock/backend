package com.bjcareer.search.repository.stock;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.bjcareer.search.domain.entity.ThemaInfo;
import com.bjcareer.search.domain.entity.ThemaInfoSearchCount;

@SpringBootTest
@Transactional  // 테스트 후 데이터 롤백
class SearchCountRepositoryTest {

	@Autowired
	private SearchCountRepository searchCountRepository;

	@Autowired
	private ThemaInfoRepository themaInfoRepository;

	@Test
	void testFindByThemaInfoNameAndDate() {
		// Given
		ThemaInfo themaInfo = new ThemaInfo("test", "test");
		themaInfoRepository.save(themaInfo);  // ThemaInfo 저장

		ThemaInfoSearchCount themaInfoSearchCount = new ThemaInfoSearchCount(themaInfo, 1L, LocalDate.now());
		searchCountRepository.save(themaInfoSearchCount);  // ThemaInfoSearchCount 저장

		// When
		Optional<ThemaInfoSearchCount> result = searchCountRepository.findByThemaInfo_NameAndDate("test",
			LocalDate.now());

		// Then
		assertTrue(result.isPresent(), "검색 결과가 존재해야 합니다.");
		assertNotNull(result.get(), "검색된 ThemaInfoSearchCount가 null이 아닙니다.");
		assertEquals(themaInfoSearchCount.getCount(), result.get().getCount(), "저장된 count 값이 동일해야 합니다.");
	}
}
