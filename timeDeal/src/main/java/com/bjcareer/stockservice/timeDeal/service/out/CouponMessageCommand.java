package com.bjcareer.stockservice.timeDeal.service.out;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class CouponMessageCommand {
	private final String sessionId;
	private final boolean result;
	private final String message;
}
