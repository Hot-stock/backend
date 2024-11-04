package com.bjcareer.search.application.port.out.persistence.themaInfo;

import java.util.Optional;

import com.bjcareer.search.domain.entity.ThemaInfo;

public interface ThemaInfoRepositoryPort {
	Optional<ThemaInfo> loadByName(String thema);
}
