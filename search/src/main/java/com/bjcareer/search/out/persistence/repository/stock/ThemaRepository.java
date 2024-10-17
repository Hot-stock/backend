package com.bjcareer.search.out.persistence.repository.stock;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.bjcareer.search.domain.entity.Thema;

public interface ThemaRepository extends JpaRepository<Thema, Long> {
	@Query("SELECT t FROM Thema t WHERE t.stock.name = :stockName AND t.themaInfo.name = :themaName")
	Optional<Thema> findByStockNameAndThemaName(String stockName, String themaName);

	@Query("SELECT t FROM Thema t join fetch t.stock s join fetch t.themaInfo ti WHERE ti.name LIKE %:keyword% ORDER BY "
		+ "CASE WHEN ti.name = :keyword THEN 1 "
		+ "WHEN ti.name LIKE :keyword% THEN 2 "
		+ "ELSE 3 END")
	List<Thema> findAllByKeywordContaining(String keyword, Pageable pageable);

	@Query("SELECT t FROM Thema t join fetch t.stock s join fetch t.themaInfo ti WHERE s.name = :keyword OR ti.name = :keyword")
	List<Thema> findAllByKeywordExactlySame(String keyword);
}
