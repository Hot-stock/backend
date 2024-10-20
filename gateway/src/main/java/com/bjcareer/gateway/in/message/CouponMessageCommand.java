package com.bjcareer.gateway.in.message;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.Data;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@Data
@ToString
@Slf4j
public class CouponMessageCommand {
	private String sessionId;
	private boolean result;
	private String message;

	public static CouponMessageCommand parseStringToObj(String message) {
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			CouponMessageCommand command = objectMapper.readValue(message, CouponMessageCommand.class);
			log.info("Get coupon response : {}", command);
			return command;
		} catch (JsonProcessingException e) {
			log.error("Failed to parse message to CouponMessageCommand : {}", message);
			throw new RuntimeException(e);
		}
	}
}
