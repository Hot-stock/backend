package com.bjcareer.GPTService.in;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import com.bjcareer.GPTService.application.GPTStockNewsService;
import com.bjcareer.GPTService.application.SaveGPTStockNewsCommand;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class KafkaConsumerController {
	private final GPTStockNewsService gptStockNewsService;
	private final KafkaTemplate<String, SaveGPTStockNewsCommand> kafkaTemplate;

	@KafkaListener(topics = "example-topic", groupId = "example-group")
	public void consume(SaveGPTStockNewsCommand command, Acknowledgment acknowledgment) {
		try {
			gptStockNewsService.saveGPTStockNews(command);
			acknowledgment.acknowledge(); // 메시지 처리 완료 후 커밋
		} catch (Exception e) {
			log.debug("Can't save Error: {} {}", e, command.link);
			kafkaTemplate.sendDefault(command);
		}
	}
}
