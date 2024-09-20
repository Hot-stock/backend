package com.bjcareer.search.repository.stock;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.bjcareer.search.domain.entity.Stock;

@Repository
public interface StockRepository extends JpaRepository<Stock, Long>{
	@Query("SELECT s FROM Stock s WHERE s.name LIKE %:name%")
	List<Stock> findByStockNameContaining(String name);

	Optional<Stock> findByName(String name);

	Optional<Stock> findByCode(String code);
}
