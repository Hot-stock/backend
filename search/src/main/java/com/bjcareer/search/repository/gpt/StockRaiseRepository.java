
package com.bjcareer.search.repository.gpt;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bjcareer.search.domain.entity.StockRaiseReasonEntity;

@Repository
public interface StockRaiseRepository extends JpaRepository<StockRaiseReasonEntity, Long> {
}
