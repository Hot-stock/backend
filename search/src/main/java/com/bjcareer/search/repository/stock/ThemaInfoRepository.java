package com.bjcareer.search.repository.stock;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.bjcareer.search.domain.entity.ThemaInfo;

public interface ThemaInfoRepository extends JpaRepository<ThemaInfo, Long> {

	@Query("SELECT t FROM ThemaInfo t join fetch t.themas s join fetch s.stock  WHERE t.name LIKE %:thema%")
	List<ThemaInfo> findByNameContains(String thema);
}
