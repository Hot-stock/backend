package com.bjcareer.GPTService.config.infra;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.redisson.config.SingleServerConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
public class RedisConfig {

	@Value("${redis.address}")
	private String address;

	@Value("${redis.password}")
	private String password;

	@Value("${redis.database}")
	private Integer database;

	@Bean
	public RedissonClient redissonClient() {
		Config config = new Config();

		SingleServerConfig singleServerConfig = config.useSingleServer();
		singleServerConfig.setAddress(address);
		singleServerConfig.setPassword(password);
		singleServerConfig.setDatabase(database);

		return Redisson.create(config);
	}
}
