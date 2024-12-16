package com.bjcareer.search.application;

import java.net.URL;
import java.time.Duration;

import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import com.bjcareer.search.application.port.out.persistence.stock.StockRepositoryPort;
import com.bjcareer.search.out.persistence.cache.RedisS3Adapter;
import com.bjcareer.search.out.s3.S3Bucket;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class S3Service {
	private final S3Bucket bucket;
	private final RedisS3Adapter redisS3Adapter;
	private final StockRepositoryPort stockRepositoryPort;

	public String getStockLogoURL(String name) {
		Pair<Boolean, String> logo = redisS3Adapter.getLogo(name);

		if(!logo.getFirst()) {
			String code = stockRepositoryPort.findByName(name).get().getCode();
			uploadURL(code, name);
			logo = redisS3Adapter.getLogo(name);
		}
		return logo.getSecond();
	}

	public boolean isExist(String code) {
		Pair<Boolean, String> logo = redisS3Adapter.getLogo(code);

		return logo.getFirst();
	}

	public void uploadURL(String code, String name) {
		String key = "logo/stock/" + code + ".png";
		URL url = bucket.generatePresignedGetUrl(key, Duration.ofDays(1));
		redisS3Adapter.updateLogo(name, url.toString(), Duration.ofDays(1));
	}
}
