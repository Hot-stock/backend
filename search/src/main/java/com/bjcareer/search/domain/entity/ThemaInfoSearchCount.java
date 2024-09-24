package com.bjcareer.search.domain.entity;

import java.time.LocalDate;
import jakarta.persistence.*;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(
	name = "thema_info_search_count",
	uniqueConstraints = {
		@UniqueConstraint(columnNames = { "THEMA_INFO_ID", "date" })
	}
)
public class ThemaInfoSearchCount {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "THEMA_INFO_ID", nullable = false)
	private ThemaInfo themaInfo;

	private Long count;

	private LocalDate date;

	public ThemaInfoSearchCount(ThemaInfo themaInfo, Long count, LocalDate date) {
		this.themaInfo = themaInfo;
		this.count = count;
		this.date = date;
	}
}
