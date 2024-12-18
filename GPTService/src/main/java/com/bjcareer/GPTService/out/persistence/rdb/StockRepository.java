package com.bjcareer.GPTService.out.persistence.rdb;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bjcareer.GPTService.domain.Stock;

@Repository
public interface StockRepository extends JpaRepository<Stock, Long> {
	Optional<Stock> findByName(String name);
}
