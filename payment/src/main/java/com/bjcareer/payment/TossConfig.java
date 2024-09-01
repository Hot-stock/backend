package com.bjcareer.payment;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;

@Getter
@ConfigurationProperties("psp.toss")
public class TossConfig {
	private String url;
	private String clientKey;
	private String secretKey;

	public TossConfig(String url, String clientKey, String secretKey) {
		this.url = url;
		this.clientKey = clientKey;
		this.secretKey = secretKey;
	}
}
