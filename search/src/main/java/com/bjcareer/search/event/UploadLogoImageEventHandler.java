package com.bjcareer.search.event;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.Channels;
import java.nio.channels.WritableByteChannel;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import com.bjcareer.search.event.messages.UpdateStockLogoMessage;
import com.bjcareer.search.out.api.toss.TossServerAdapter;
import com.bjcareer.search.out.api.toss.dtos.TossStockInfoDTO;
import com.bjcareer.search.out.s3.S3Bucket;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class UploadLogoImageEventHandler {
	private final S3Bucket s3Bucket;
	private final TossServerAdapter tossServerAdapter;
	private final WebClient webClient;
	private static final String BUCKET_KEY = "logo/stock/";

	public UploadLogoImageEventHandler(S3Bucket bucket, TossServerAdapter tossServerAdapter) {
		this.s3Bucket = bucket;
		this.tossServerAdapter = tossServerAdapter;
		this.webClient = WebClient.builder()
			.codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(10 * 1024 * 1024))
			.build();
	}

	@Async
	@EventListener
	public void handle(UpdateStockLogoMessage event) {
		String stockCode = event.getStock().getCode();
		log.info("Processing stock: {}", stockCode);

		TossStockInfoDTO stockInfo = tossServerAdapter.getStockInfo(stockCode);
		if (stockInfo.getResult() == null) {
			log.error("Failed to fetch information from Toss for stock: {}", stockCode);
			return;
		}

		String logoUrl = stockInfo.getResult().getLogoImageUrl();
		String localFilePath = "./logo/" + stockCode + ".png";

		// 로컬 디렉토리 확인 및 생성
		createDirectoryIfNotExists("./logo");

		// 이미지 저장
		saveImage(logoUrl, localFilePath);

		// S3 업로드
		File file = new File(localFilePath);
		if (file.exists()) {
			try {
				String s3Key = BUCKET_KEY + stockCode + ".png";
				s3Bucket.uploadFile(file, s3Key);
				log.info("Uploaded to S3: {}", s3Key);

				// 업로드 성공 시 파일 삭제
				if (file.delete()) {
					log.info("Deleted local file: {}", localFilePath);
				} else {
					log.warn("Failed to delete local file: {}", localFilePath);
				}
			} catch (Exception e) {
				log.error("Error during S3 upload for stock {}: {}", stockCode, e.getMessage());
			}
		} else {
			log.error("File not found for S3 upload: {}", localFilePath);
		}
	}

	public void saveImage(String imageUrl, String destinationFile) {
		try {
			byte[] imageData = webClient.get()
				.uri(imageUrl)
				.retrieve()
				.bodyToMono(byte[].class)
				.block();

			if (imageData == null) {
				throw new IOException("No data received from: " + imageUrl);
			}

			saveToFile(imageData, destinationFile);
			log.info("Image saved locally at {}", destinationFile);
		} catch (Exception e) {
			log.error("Error saving image from URL {}: {}", imageUrl, e.getMessage());
		}
	}

	private void saveToFile(byte[] data, String destinationFile) throws IOException {
		try (FileOutputStream fos = new FileOutputStream(destinationFile);
			 WritableByteChannel channel = Channels.newChannel(fos)) {
			channel.write(java.nio.ByteBuffer.wrap(data));
		}
	}

	private void createDirectoryIfNotExists(String directoryPath) {
		try {
			Files.createDirectories(Paths.get(directoryPath));
			log.info("Directory ensured: {}", directoryPath);
		} catch (IOException e) {
			log.error("Error creating directory {}: {}", directoryPath, e.getMessage());
		}
	}
}
