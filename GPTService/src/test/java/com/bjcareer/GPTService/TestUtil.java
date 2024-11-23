package com.bjcareer.GPTService;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class TestUtil {
	public static final String PUB_DATE = LocalDateTime.now()
		.atZone(ZoneId.of("GMT"))
		.format(DateTimeFormatter.ofPattern("EEE, dd MMM yyyy HH:mm:ss VV", Locale.ENGLISH));

}
