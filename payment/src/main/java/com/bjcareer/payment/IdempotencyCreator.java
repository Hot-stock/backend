package com.bjcareer.payment;

import java.util.UUID;

public class IdempotencyCreator {

	public static String create(Long cartId){
		return UUID.nameUUIDFromBytes(cartId.toString().getBytes()).toString();
	}
}
