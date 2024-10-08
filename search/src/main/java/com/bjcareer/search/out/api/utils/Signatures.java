package com.bjcareer.search.out.api.utils;

import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.security.SignatureException;
import java.util.Base64;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class Signatures {
	private static final String HMAC_SHA256 = "HmacSHA256";  // Java 표준 알고리즘 이름

	public static String of(String timestamp, String method, String resource, String key) throws SignatureException {
		return of(timestamp + "." + method + "." + resource, key);
	}

	public static String of(String data, String key) throws SignatureException {
		try {
			Mac mac = Mac.getInstance(HMAC_SHA256);  // 기본 프로바이더 사용

			mac.init(new SecretKeySpec(key.getBytes("utf-8"), HMAC_SHA256));
			byte[] rawHmac = mac.doFinal(data.getBytes());

			return Base64.getEncoder().encodeToString(rawHmac);
		} catch (GeneralSecurityException e) {
			throw new SignatureException("Failed to generate signature.", e);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}
}
