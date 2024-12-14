package com.bjcareer.search.out.s3;

import java.io.File;
import java.net.URL;
import java.nio.file.Path;
import java.time.Duration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;

@Component
@RequiredArgsConstructor
public class S3Bucket {
	@Value("${aws.s3.bucket}")
	private String bucketName;
	private final S3Client s3Client;
	private final S3Presigner s3Presigner;

	public void uploadFile(File file, String key) {
		s3Client.putObject(
			PutObjectRequest.builder()
				.bucket(bucketName)
				.key(key)
				.build(),
			RequestBody.fromFile(file)
		);
	}


	public URL generatePresignedGetUrl(String key, Duration expiration) {
		GetObjectRequest getObjectRequest = GetObjectRequest.builder()
			.bucket(bucketName)
			.key(key)
			.build();

		PresignedGetObjectRequest presignedGetObjectRequest = s3Presigner.presignGetObject(builder -> builder
			.getObjectRequest(getObjectRequest)
			.signatureDuration(expiration));

		return presignedGetObjectRequest.url();
	}

	public File downloadFile(String key, String downloadPath) {
		s3Client.getObject(
			GetObjectRequest.builder()
				.bucket(bucketName)
				.key(key)
				.build(),
			Path.of(downloadPath)
		);
		return new File(downloadPath);
	}
}
