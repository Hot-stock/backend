package com.bjcareer.search.repository.stock;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.bjcareer.search.domain.entity.ThemaInfoSearchCount;

public interface SearchCountRepository extends JpaRepository<ThemaInfoSearchCount, Long> {
	Optional<ThemaInfoSearchCount> findByThemaInfo_NameAndDate(String themaInfoName, LocalDate date);
}
