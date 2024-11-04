package com.bjcareer.search.out.persistence.repository.stock;

import java.util.Collection;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.bjcareer.search.domain.entity.Stock;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class StockRepositoryAdapter {
	@PersistenceContext
	private EntityManager em;

	public Stock findByCode(String code) {
		return em.createQuery(StockQuery.FIND_STOCK_BY_CODE, Stock.class)
			.setParameter("code", code)
			.getSingleResult();
	}

	public List<Stock> findAll() {
		return em.createQuery(StockQuery.FIND_ALL, Stock.class)
			.getResultList();
	}

	public void save(Stock stock) {
		em.persist(stock);
	}

	public void saveALl(Collection<Stock> stocks) {
		for (Stock stock : stocks) {
			em.persist(stock);
		}
	}
}
