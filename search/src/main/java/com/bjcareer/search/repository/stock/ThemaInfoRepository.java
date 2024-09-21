package com.bjcareer.search.repository.stock;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bjcareer.search.domain.entity.ThemaInfo;

public interface ThemaInfoRepository extends JpaRepository<ThemaInfo, Long> {
	Optional<ThemaInfo> findByName(String thema);
}
