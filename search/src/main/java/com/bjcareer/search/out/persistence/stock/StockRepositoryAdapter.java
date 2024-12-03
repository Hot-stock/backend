package com.bjcareer.search.out.persistence.stock;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.bjcareer.search.application.port.out.persistence.stock.LoadStockCommand;
import com.bjcareer.search.application.port.out.persistence.stock.StockRepositoryPort;
import com.bjcareer.search.domain.entity.Stock;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class StockRepositoryAdapter implements StockRepositoryPort {
	@PersistenceContext
	private EntityManager em;

	@Override
	public Optional<Stock> findByName(String name) {
		List<Stock> results = em.createQuery(Query.FIND_STOCK_BY_NAME, Stock.class)
			.setParameter("name", name)
			.getResultList();

		return results.stream().findFirst();
	}

	@Override
	public Optional<Stock> findByCode(String name) {
		List<Stock> results = em.createQuery(Query.FIND_STOCK_BY_CODE, Stock.class)
			.setParameter("code", name)
			.getResultList();

		return results.stream().findFirst();
	}

	public List<Stock> findAll() {
		return em.createQuery(Query.FIND_ALL, Stock.class)
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

	@Override
	public List<Stock> loadAllByKeywordContaining(LoadStockCommand command) {
		return em.createQuery(Query.FILTER_STOCK_BY_KEYWORD, Stock.class)
			.setParameter("keyword", command.getKeyword() + "%")
			.getResultList();
	}
}
