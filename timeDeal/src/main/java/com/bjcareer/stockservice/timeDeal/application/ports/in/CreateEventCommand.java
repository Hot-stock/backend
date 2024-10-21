package com.bjcareer.stockservice.timeDeal.application.ports.in;

import lombok.Data;

@Data
public class CreateEventCommand {
	private final int publishedCouponNum;
	private final int discountRate;
}
