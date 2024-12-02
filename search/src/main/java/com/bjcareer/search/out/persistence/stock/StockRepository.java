package com.bjcareer.search.out.persistence.stock;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bjcareer.search.domain.entity.Stock;

@Repository
public interface StockRepository extends JpaRepository<Stock, Long>{
	Optional<Stock> findByCode(String code);
	Optional<Stock> findByName(String name);
}
