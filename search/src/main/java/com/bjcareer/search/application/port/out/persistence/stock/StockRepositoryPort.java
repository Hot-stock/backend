package com.bjcareer.search.application.port.out.persistence.stock;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import com.bjcareer.search.domain.entity.Stock;

public interface StockRepositoryPort {
	Optional<Stock> findByName(String name);

	Optional<Stock> findByCode(String code);

	List<Stock> findAll();

	void save(Stock stock);

	void saveALl(Collection<Stock> stocks);
}
