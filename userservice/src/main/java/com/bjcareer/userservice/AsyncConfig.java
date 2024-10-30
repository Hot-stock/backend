package com.bjcareer.userservice;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
public class AsyncConfig {

	@Bean
	public TaskExecutor taskExecutor() {

		ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
		taskExecutor.setCorePoolSize(10);
		taskExecutor.setQueueCapacity(20);
		taskExecutor.setMaxPoolSize(30);
		taskExecutor.setTaskDecorator(new MdcTaskDecorator());

		return taskExecutor;
	}
}
