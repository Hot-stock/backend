package com.bjcareer.search.in.api.controller;

import java.util.Optional;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;

import com.bjcareer.search.application.port.in.UpdateThemaOfStockCommand;
import com.bjcareer.search.application.thema.ThemaService;
import com.bjcareer.search.in.api.controller.dto.ThemaInfoResponseDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class KafkaConsumerController {
	private final ThemaService themaService;
	private final ObjectMapper objectMapper = new ObjectMapper();

	@KafkaListener(topics = "thema-topic", groupId = "thema-topic-group-1")
	public void consumeAnalyzeStock(String message, Acknowledgment acknowledgment) {
		Optional<ThemaInfoResponseDTO> optionalCommand = getCommand(message);

		if (optionalCommand.isEmpty()) {
			return;
		}

		ThemaInfoResponseDTO command = optionalCommand.get();
		try {
			UpdateThemaOfStockCommand updateThemaOfStockCommand = new UpdateThemaOfStockCommand(command.getStockName(),
				command.getName());
			themaService.updateThema(updateThemaOfStockCommand);
		} catch (Exception e) {
			log.debug("Can't save Error: {} {}", command.getName(), command.getStockName());
		} finally {
			acknowledgment.acknowledge(); // 메시지 처리 완료 후 커밋
		}
	}

	private Optional<ThemaInfoResponseDTO> getCommand(String message) {
		try {
			return Optional.of(objectMapper.readValue(message, ThemaInfoResponseDTO.class));
		} catch (JsonProcessingException e) {
			log.error("Error: {}", e);
		}

		return Optional.empty();
	}
}
