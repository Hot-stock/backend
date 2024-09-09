package com.bjcareer.payment.application.port.in;

import lombok.Data;

@Data
public class ValidationCheckoutCommand {
	private final String buyerPK;
	private final String checkoutId;
}
