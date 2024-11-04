package com.bjcareer.search.application.port.out.persistence.thema;

import java.util.List;
import java.util.Optional;

import com.bjcareer.search.domain.entity.Thema;

public interface ThemaRepositoryPort {
	Optional<Thema> loadByStockNameAndThemaName(LoadStockByThemaCommand command);

	List<Thema> loadAllByKeywordContaining(LoadThemaUsingkeywordCommand command);

	void save(Thema thema);
}
