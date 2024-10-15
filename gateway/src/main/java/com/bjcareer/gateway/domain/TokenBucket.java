package com.bjcareer.gateway.domain;

import java.time.Instant;

import com.bjcareer.gateway.common.Logger;
import com.bjcareer.gateway.exceptions.TooManyRequestsException;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Getter
public class TokenBucket {
	public static final int MAX_REQUESTS = 10;
	public static final int REFILL_INTERVAL_SECONDS = 60 * 5;

	private long lastRequestTimestamp; // 마지막 요청 시간 (초 단위)
	private int availableTokens;  // 남은 API 호출 가능 횟수 (토큰)

	public TokenBucket() {
		this.lastRequestTimestamp = getCurrentTimestamp();
		this.availableTokens = MAX_REQUESTS;
	}

	// API 호출 메서드
	public void attemptApiCall(Logger log) {
		refillTokens(log);  // 토큰 리필

		if (isRateLimitExceeded()) {
			throw new TooManyRequestsException("API 호출 횟수 초과");
		}

		availableTokens--;  // API 호출 시 토큰 감소
		lastRequestTimestamp = getCurrentTimestamp();  // 마지막 호출 시간 업데이트
	}

	// 호출 제한이 초과되었는지 확인
	private boolean isRateLimitExceeded() {
		return availableTokens <= 0;
	}

	// 남은 호출 가능 횟수(토큰)를 리필하는 메서드
	private void refillTokens(Logger log) {
		long currentTimestamp = getCurrentTimestamp();  // 현재 시간 (초 단위)
		long elapsedTimeSinceLastCall = currentTimestamp - lastRequestTimestamp;  // 마지막 요청으로부터 경과한 시간

		log.debug("elapsedTimeSinceLastCall = {}", currentTimestamp);
		log.debug("elapsedTimeSinceLastCall = {}", elapsedTimeSinceLastCall);
		// 5분(300초) 단위로 경과 시간을 계산하여 리필할 토큰 수 계산
		long refillableTokens = elapsedTimeSinceLastCall / REFILL_INTERVAL_SECONDS;
		log.debug("refillableTokens = {}", refillableTokens);

		// 남은 호출 가능 횟수를 리필하되 최대치를 넘지 않도록 제한
		availableTokens = (int)Math.min(MAX_REQUESTS, availableTokens + refillableTokens);
	}

	private long getCurrentTimestamp() {
		return Instant.now().getEpochSecond();
	}
}
