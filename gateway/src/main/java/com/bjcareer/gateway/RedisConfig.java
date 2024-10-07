package com.bjcareer.gateway;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.redisson.config.SingleServerConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class RedisConfig {

	@Value("${redis.address}")
	private String redisAddress;

	@Value("${redis.password}")
	private String redisPassword;

	@Value("${redis.database}")
	private int redisDatabase;

	@Bean
	public RedissonClient createRedisConfig() {
		Config config = new Config();
		SingleServerConfig singleServerConfig = config.useSingleServer();

		singleServerConfig.setAddress(redisAddress);
		singleServerConfig.setPassword(redisPassword);
		singleServerConfig.setDatabase(redisDatabase);

		return Redisson.create(config);
	}
}
