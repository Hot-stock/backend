package com.bjcareer.search.out.persistence.repository.chart;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bjcareer.search.domain.entity.StockChart;

public interface StockChartRepository extends JpaRepository<StockChart, Long> {
}
