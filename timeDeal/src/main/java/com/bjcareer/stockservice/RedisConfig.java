package com.bjcareer.stockservice;

import org.redisson.api.RScoredSortedSet;
import org.redisson.api.RSortedSet;
import org.redisson.config.MasterSlaveServersConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.client.codec.StringCodec;
import org.redisson.config.Config;
import org.redisson.api.RPatternTopic;
import org.springframework.data.util.Pair;

import com.bjcareer.stockservice.timeDeal.domain.event.Event;
import com.bjcareer.stockservice.timeDeal.listener.RedisListenerService;
import com.bjcareer.stockservice.timeDeal.repository.CouponRepository;
import com.bjcareer.stockservice.timeDeal.repository.EventRepository;
import com.bjcareer.stockservice.timeDeal.repository.InMemoryEventRepository;

@Configuration
public class RedisConfig {

	@Value("${redis.address.master}")
	private String redisMasterAddress;

	@Value("${redis.address.slave}")
	private String redisSlaveAddress;

	@Value("${redis.password}")
	private String redisPassword;

	@Value("${redis.database}")
	private int redisDatabase;

	@Bean
	public RedissonClient redissonClient() {
		Config config = new Config();

		MasterSlaveServersConfig masterSlaveServersConfig = config.useMasterSlaveServers();

		// 마스터 주소 설정
		masterSlaveServersConfig.setMasterAddress(redisMasterAddress);
		masterSlaveServersConfig.setDatabase(redisDatabase);
		masterSlaveServersConfig.setPassword(redisPassword);

		// 슬레이브 주소 설정
		masterSlaveServersConfig.addSlaveAddress(redisSlaveAddress);

		return Redisson.create(config);
	}

	@Bean
	public Object setupKeyExpirationListener(RedissonClient redissonClient, RedisListenerService listenerService) {
		String patternTopic = String.format("__keyevent@%d__:expired", redisDatabase);
		RPatternTopic topic = redissonClient.getPatternTopic(patternTopic, StringCodec.INSTANCE);
		topic.addListener(String.class, listenerService);
		return null;
	}

	@Bean
	public RedisListenerService setUpredisListenerService(InMemoryEventRepository memoryEventRepository, EventRepository repository, CouponRepository couponRepository) {
		RedisListenerService redisListenerService = new RedisListenerService(memoryEventRepository, repository,
			couponRepository);
		return redisListenerService;
	}
}
