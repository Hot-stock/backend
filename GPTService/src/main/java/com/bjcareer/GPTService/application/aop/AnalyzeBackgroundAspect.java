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
import com.bjcareer.GPTService.out.persistence.document.GPTStockNewsRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class AnalyzeBackgroundAspect {
	private final ApplicationEventPublisher applicationEventPublisher;

	@Pointcut("@annotation(com.bjcareer.GPTService.application.aop.AnalyzeBackground)")
	private void cut() {
	}

	@AfterReturning(pointcut = "cut()", returning = "result")
	public void extractThemaInStockNew(JoinPoint joinPoint, Object result) {
		Signature signature = joinPoint.getSignature();
		log.debug("extractThemaInStockNew invoked for method: {}", signature.getName());

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
		if (!(result.isRelated() & result.isThema())) {
			return;
		}

		applicationEventPublisher.publishEvent(result);
	}
}
