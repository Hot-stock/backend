package com.bjcareer.search.repository.stock;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.bjcareer.search.domain.entity.Stock;
import com.bjcareer.search.domain.entity.Thema;

@SpringBootTest
@Transactional
class StockRepositoryTest {
	@Autowired ThemaRepository themaRepository;

	@Test
	void test_주식_이름으로_관련된_테마정보_조회() {
		List<Thema> themas = themaRepository.findByStockName("덕");

		themas.forEach(thema -> {
			System.out.println(thema);
		});
	}

}