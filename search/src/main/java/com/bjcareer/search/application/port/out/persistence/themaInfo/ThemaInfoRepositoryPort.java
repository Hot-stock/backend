package com.bjcareer.search.application.port.out.persistence.themaInfo;

import java.util.List;
import java.util.Optional;

import com.bjcareer.search.domain.entity.ThemaInfo;

public interface ThemaInfoRepositoryPort {
	Optional<ThemaInfo> loadByName(String thema);
	ThemaInfo save(ThemaInfo themaInfo);
	List<ThemaInfo> findAll();
	Optional<ThemaInfo> findById(Long id);
	void saveAll(List<ThemaInfo> themaInfoList);
	Optional<ThemaInfo> findByName(String thema);

}
