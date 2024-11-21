package com.bjcareer.gateway.common;

import static org.springframework.context.annotation.ScopedProxyMode.*;
import static org.springframework.web.context.WebApplicationContext.*;

import org.slf4j.MDC;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Scope(value = SCOPE_REQUEST, proxyMode = TARGET_CLASS)
@Component
@Slf4j
public class Logger {
	@Setter
	private String url;

	public void debug(String format, Object... arguments) {
		if (log.isDebugEnabled()) {
			String formatted = format.formatted(arguments);
			log.debug("%s %s".formatted(getPrefix(), formatted));
		}
	}

	public void info(String format, Object... arguments) {
		if (log.isInfoEnabled()) {
			String formatted = format.formatted(arguments);
			log.info("%s %s".formatted(getPrefix(), formatted));
		}
	}

	public void warn(String format, Object... arguments) {
		if (log.isWarnEnabled()) {
			String formatted = format.formatted(arguments);
			log.warn("%s %s".formatted(getPrefix(), formatted));
		}
	}

	public void error(String format, Object... arguments) {
		if (log.isErrorEnabled()) {
			String formatted = format.formatted(arguments);
			log.error("%s %s".formatted(getPrefix(), formatted));
		}
	}

	@PostConstruct
	public void init() {
		String uuid = java.util.UUID.randomUUID().toString().substring(0, 8);
		MDC.put("traceId", uuid);
	}

	private String getPrefix() {
		return "[" + this.url + "] ";
	}
}
