package com.bjcareer.GPTService.in;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import com.bjcareer.GPTService.application.GPTStockNewsService;
import com.bjcareer.GPTService.application.port.in.AnalyzeStockNewsCommand;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class KafkaConsumerController {
	private final GPTStockNewsService gptStockNewsService;
	private final KafkaTemplate<String, AnalyzeStockNewsCommand> kafkaTemplate;

	@KafkaListener(topics = "news-stock-topic", groupId = "news-stock-group")
	public void consume(AnalyzeStockNewsCommand command, Acknowledgment acknowledgment) {
		try {
			gptStockNewsService.saveGPTStockNews(command);
			acknowledgment.acknowledge(); // 메시지 처리 완료 후 커밋
		} catch (Exception e) {
			log.debug("Can't save Error: {} {}", e, command.newLink);
			kafkaTemplate.sendDefault(command);
		}
	}
}
