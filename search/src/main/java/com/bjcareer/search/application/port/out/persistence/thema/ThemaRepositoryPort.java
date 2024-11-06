package com.bjcareer.search.application.port.out.persistence.thema;

import java.util.List;
import java.util.Optional;

import com.bjcareer.search.domain.entity.Thema;
import com.bjcareer.search.domain.entity.ThemaInfo;

public interface ThemaRepositoryPort {
	Optional<Thema> loadByStockNameAndThemaName(LoadStockByThemaCommand command);

	List<Thema> loadAllByKeywordContaining(LoadThemaUsingkeywordCommand command);

	Thema save(Thema thema);

	List<Thema> findAll();

	void saveAll(List<Thema> themaList);

	Optional<ThemaInfo> findByName(String thema);
}
