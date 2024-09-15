package com.bjcareer.search.repository.stock;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bjcareer.search.domain.entity.Stock;

public interface StockRepository extends JpaRepository<Stock, Long>{
}
