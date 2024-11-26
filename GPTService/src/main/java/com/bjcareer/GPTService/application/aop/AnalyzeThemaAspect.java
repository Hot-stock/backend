package com.bjcareer.GPTService.application.aop;

import java.util.List;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import com.bjcareer.GPTService.domain.gpt.GPTNewsDomain;
import com.bjcareer.GPTService.out.persistence.document.GPTThemaNewsRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class AnalyzeThemaAspect {
	private final ApplicationEventPublisher applicationEventPublisher;
	private final GPTThemaNewsRepository gptThemaNewsRepository;

	@Pointcut("@annotation(com.bjcareer.GPTService.application.aop.AnalyzeThema)")
	private void cut() {
	}

	@AfterReturning(pointcut = "cut()", returning = "result")
	public void rateLimiting(JoinPoint joinPoint, Object result) {
		Signature signature = joinPoint.getSignature();
		log.debug("Rate Limiting Aspect invoked for method: {}", signature.getName());

		if (result instanceof GPTNewsDomain) {
			isAlreadyExtract((GPTNewsDomain)result);
		} else if (result instanceof List) {
			((List<?>)result).stream()
				.filter(item -> item instanceof GPTNewsDomain) // 타입 확인
				.map(item -> (GPTNewsDomain)item) // 안전한 캐스팅
				.forEach(this::isAlreadyExtract); // 이벤트 발행
		}
	}

	private void isAlreadyExtract(GPTNewsDomain result) {
		if (gptThemaNewsRepository.findByLink(result.getLink()).isEmpty()) {
			applicationEventPublisher.publishEvent(result);
		}
	}
}
