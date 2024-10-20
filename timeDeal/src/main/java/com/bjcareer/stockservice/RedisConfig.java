package com.bjcareer.stockservice;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.redisson.config.SingleServerConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RedisConfig {

	@Value("${redis.address}")
	private String redisMasterAddress;

	@Value("${redis.password}")
	private String redisPassword;

	@Value("${redis.database}")
	private int redisDatabase;

	@Bean
	public RedissonClient redissonClient() {
		Config config = new Config();

		SingleServerConfig singleServerConfig = config.useSingleServer();

		// 마스터 주소 설정
		singleServerConfig.setAddress(redisMasterAddress);
		singleServerConfig.setDatabase(redisDatabase);
		singleServerConfig.setPassword(redisPassword);

		return Redisson.create(config);
	}

}
