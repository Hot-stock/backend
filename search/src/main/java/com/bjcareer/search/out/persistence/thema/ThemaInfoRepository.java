package com.bjcareer.search.out.persistence.thema;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bjcareer.search.domain.entity.ThemaInfo;

public interface ThemaInfoRepository extends JpaRepository<ThemaInfo, Long> {
	Optional<ThemaInfo> findByName(String thema);
}
