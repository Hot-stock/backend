package com.bjcareer.search.application;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class S3ServiceTest {
	@Autowired
	S3Service s3Service;

	@Test
	void 로고이미지요청() {
		String stockLogoURL = s3Service.getStockLogoURL("삼성전자");
		assertNotNull(stockLogoURL);
		System.out.println("stockLogoURL = " + stockLogoURL);
	}

	@Test
	void 로고이미지업로드요청() {
		s3Service.uploadURL("005930", "삼성전자");
		String stockLogoURL = s3Service.getStockLogoURL("삼성전자");
		assertNotNull(stockLogoURL);
	}
}
