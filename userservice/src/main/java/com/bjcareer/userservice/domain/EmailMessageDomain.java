package com.bjcareer.userservice.domain;

import org.springframework.mail.javamail.MimeMessageHelper;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class EmailMessageDomain {
	// 상수로 인증 코드를 위한 변수명을 설정
	public static final String VERIFICATION_CODE = "verificationCode";
	public static final String REGISTER_TEMPLATE = "registerEmail.html";

	// 이메일 관련 정보 및 템플릿 엔진 의존성
	private final MimeMessage message;
	private final String toEmail;
	private final Long verificationCode;
	private final TemplateEngine templateEngine;

	public EmailMessageDomain(MimeMessage message, String toEmail, Long verificationCode,
		TemplateEngine templateEngine) {
		this.message = message;
		this.toEmail = toEmail;
		this.verificationCode = verificationCode;
		this.templateEngine = templateEngine;
	}

	/**
	 * MIME 메시지를 생성하여 HTML 형식으로 설정합니다.
	 */
	public void createMIMEMessage() {
		// 컨텍스트에 인증 코드 변수 설정
		String htmlContent = makeHtmlTemplate();

		// MIME 메시지 설정
		try {
			MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(message, true, "UTF-8");
			mimeMessageHelper.setTo(toEmail);
			mimeMessageHelper.setSubject("Verification code");
			mimeMessageHelper.setText(htmlContent, true); // HTML 형식 설정
			mimeMessageHelper.setFrom("noreply@next-stock.com");
		} catch (MessagingException e) {
			log.error("Error creating MIME message for recipient '{}': {}", toEmail, e.getMessage());
			throw new RuntimeException("Failed to create MIME message", e);
		}
	}

	private String makeHtmlTemplate() {
		Context context = new Context();
		context.setVariable(VERIFICATION_CODE, verificationCode);

		// 템플릿 엔진을 통해 HTML 컨텐츠 생성
		return templateEngine.process(REGISTER_TEMPLATE, context);
	}
}
