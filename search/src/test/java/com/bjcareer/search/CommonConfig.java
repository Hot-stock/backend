package com.bjcareer.search;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class CommonConfig {
	static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE, dd MMM yyyy HH:mm:ss VV", Locale.ENGLISH);

	public static String createPubDate() {
		return ZonedDateTime.now().format(formatter);
	}
}
