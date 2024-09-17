package com.bjcareer.search;

import java.util.ArrayList;
import java.util.List;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.redisson.config.SingleServerConfig;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
public class RedisConfig {

	@Value("#{'${redis.address}'.split(',')}")
	private List<String> adddress;

	@Value("${redis.password}")
	private String password;

	@Value("${redis.database}")
	private Integer database;

	@Bean
	@Qualifier("primaryRedissonClient")
	public List<RedissonClient> redissonClient(){
		List<RedissonClient> redissonClients = new ArrayList<>();

		for (String address : adddress) {
			Config config = new Config();
			SingleServerConfig singleServerConfig = config.useSingleServer();
			singleServerConfig.setAddress(address);
			singleServerConfig.setPassword(password);
			singleServerConfig.setDatabase(database);

			redissonClients.add(Redisson.create(config));
		}

		log.debug("redissonClients = {}", redissonClients);
		return redissonClients;
	}

	@Bean
	public RedissonClient mock(List<RedissonClient> redissonClients){
		return redissonClients.get(0);
	}
}
