CREATE TABLE payment_event (
           payment_event_id BIGSERIAL PRIMARY KEY,
           buyer_id VARCHAR(255) NOT NULL,
           order_id VARCHAR(255) NOT NULL,
           payment_method VARCHAR(50) NOT NULL,
           is_payment_done BOOLEAN NOT NULL,
           created_at TIMESTAMP NOT NULL,
           updated_at TIMESTAMP NOT NULL
);


CREATE TABLE payment_order (
           payment_order_id BIGSERIAL PRIMARY KEY,
           payment_event_id BIGINT NOT NULL,
           product_id BIGINT NOT NULL,
           payment_order_status VARCHAR(50) NOT NULL,
           ledger_update BOOLEAN NOT NULL,
           wallet_update BOOLEAN NOT NULL,
           fail_count INT NOT NULL,
           threshold INT NOT NULL,
           create_at TIMESTAMP NOT NULL,
           updated_at TIMESTAMP NOT NULL,
           FOREIGN KEY (payment_event_id) REFERENCES payment_event(payment_event_id) ON DELETE CASCADE
);

CREATE TABLE payment_order_history (
       payment_order_history_id SERIAL PRIMARY KEY,
       payment_order_id BIGINT NOT NULL,
       previous_status VARCHAR(50),
       new_status VARCHAR(50),
       created_at TIMESTAMP NOT NULL,
       updated_at TIMESTAMP NOT NULL,
       changed_by VARCHAR(255),
       reason TEXT,

       CONSTRAINT fk_payment_event
           FOREIGN KEY (payment_order_id)
               REFERENCES payment_order(payment_order_id)
               ON DELETE CASCADE
);

CREATE TABLE payment_coupon (
        id BIGSERIAL PRIMARY KEY,
        coupon_id BIGINT NOT NULL,
        payment_event_id BIGINT NOT NULL,
        percentage INT NOT NULL,
        FOREIGN KEY (payment_event_id) REFERENCES payment_event(payment_event_id) ON DELETE CASCADE
);