package com.bjcareer.search.repository.stock;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.bjcareer.search.domain.entity.Stock;

@Repository
public interface StockRepository extends JpaRepository<Stock, Long>{

	@Query("SELECT s FROM Stock s WHERE s.name LIKE %:name%")
	Stock findByStockName(String name);
}
