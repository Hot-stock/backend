package com.bjcareer.stockservice;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class TopicConfig {
	public static final String COUPON_TOPIC = "coupon-topic";

	@Bean
	public NewTopic topic() {
		return TopicBuilder.name(TopicConfig.COUPON_TOPIC).partitions(1).replicas(1).build();
	}
}
