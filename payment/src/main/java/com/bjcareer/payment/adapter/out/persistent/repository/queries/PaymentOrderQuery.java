package com.bjcareer.payment.adapter.out.persistent.repository.queries;

public class PaymentOrderQuery {
	public static final String PAYMENT_EVENT_ID = "payment_event_id";
	public static final String PAYMENT_ORDER_ID = "payment_order_id";
	public static final String PAYMENT_KEY = "payment_key";
	public static final String CHECKOUT_ID = "checkout_id";
	public static final String FAIL_COUNT = "fail_count";
	public static final String THRESHOLD = "threshold";
	public static final String PAYMENT_ORDER_STATUS = "payment_order_status";
	public static final String TOTAL_AMOUNT = "totalAmount";

	public static final String FIND_PENDING_PAYMENT = "SELECT " +
		"pe.payment_event_id AS payment_event_id, " +
		"pe.payment_key AS payment_key, " +
		"pe.checkout_id AS checkout_id, " +
		"po.payment_order_id AS payment_order_id, " +
		"po.payment_order_status AS payment_order_status, " +
		"po.fail_count AS fail_count, " +
		"po.threshold AS threshold " +
		"FROM payment_event pe " +
		"INNER JOIN payment_order po ON pe.payment_event_id = po.payment_event_id " +
		"WHERE po.payment_order_status = :status";

	public static final String TOTAL_AMOUNT_OF_PAYMENT_EVENT_ID = "SELECT " +
		"SUM(po.amount) AS totalAmount " +
		"FROM payment_order po " +
		"WHERE po.payment_event_id = :payment_event_id " +
		"GROUP BY po.payment_event_id";
}
