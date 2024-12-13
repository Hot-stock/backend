package com.bjcareer.search.application.thema;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bjcareer.search.application.port.in.UpdateThemaOfStockCommand;
import com.bjcareer.search.application.port.out.persistence.stock.StockRepositoryPort;
import com.bjcareer.search.application.port.out.persistence.thema.ThemaRepositoryPort;
import com.bjcareer.search.application.port.out.persistence.themaInfo.ThemaInfoRepositoryPort;
import com.bjcareer.search.domain.entity.Stock;
import com.bjcareer.search.domain.entity.Thema;
import com.bjcareer.search.domain.entity.ThemaInfo;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class ThemaService {
	private final ThemaRepositoryPort themaRepositoryPort;
	private final ThemaInfoRepositoryPort themaInfoRepositoryPort;
	private final StockRepositoryPort stockRepositoryPort;

	@Transactional
	public void updateThema(UpdateThemaOfStockCommand command) {
		log.debug("Update Thema: {}", command);

		Optional<ThemaInfo> optionalThemaInfo = themaInfoRepositoryPort.findByName(command.getName());
		ThemaInfo themaInfo = optionalThemaInfo.orElseGet(
			() -> themaInfoRepositoryPort.save(new ThemaInfo(command.getName())));

		List<Stock> stocks = command.getStockName()
			.stream()
			.map(stockRepositoryPort::findByName)
			.filter(Optional::isPresent)
			.map(Optional::get)
			.toList();

		for (Stock stock : stocks) {
			Optional<Thema> byName = themaRepositoryPort.findByName(themaInfo.getName(), stock.getName());

			if (byName.isEmpty()) {
				themaRepositoryPort.save(new Thema(stock, themaInfo));
			}
		}
	}

}
