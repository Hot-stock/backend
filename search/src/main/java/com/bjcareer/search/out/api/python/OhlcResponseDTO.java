package com.bjcareer.search.out.api.python;

import java.time.LocalDate;

import lombok.Data;

@Data
public class OhlcResponseDTO {
	private LocalDate date;
	private int open;
	private int high;
	private int low;
	private int close;
	private Long volume;
}


