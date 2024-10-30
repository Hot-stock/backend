package com.bjcareer.userservice;

import java.io.IOException;
import java.util.UUID;

import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;

@Component
public class MDCFilter implements Filter {
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		Filter.super.init(filterConfig);
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
		throws IOException, ServletException {
		// traceId를 UUID로 생성하여 설정
		String traceId = UUID.randomUUID().toString().substring(0, 8);
		MDC.put("traceId", traceId);

		try {
			chain.doFilter(request, response);
		} finally {
			// 요청이 끝나면 MDC에서 traceId를 삭제
			MDC.remove("traceId");
		}
	}

	@Override
	public void destroy() {
		Filter.super.destroy();
	}
}
