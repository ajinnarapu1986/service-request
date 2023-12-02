package com.sr.utility;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import com.sr.requestinfo.RequestInfo;

import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RequiredArgsConstructor
public class MailUtility {

	private final JavaMailSender mailSender;

	@Value("${spring.mail.username}")
	private String fromAddress;

	private static final boolean html = true;

	/**
	 * 
	 * @param requestInfoId
	 */
	public void sendApprovalMail(RequestInfo requestInfo) {
		try {
			MimeMessage message = mailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message);

			helper.setSubject("Approval Request : Request Info Id - " + requestInfo.getId());
			helper.setFrom(fromAddress);
			helper.setTo(fromAddress);

			helper.setText("<p>There is a new open request for approval in your inbox.<p>"
					+ "Please login to the Service Portal to approve the request with Request ID " + requestInfo.getId()
					+ "</a><p>Thank you,</p>", html);

			mailSender.send(message);
		} catch (Exception e) {
			log.error(":: Error in sendApprovalMail() ::", e);
		}
	}

	/**
	 * 
	 * @param requestInfoId
	 */
	public void sendEscalationMail(RequestInfo requestInfo, String level) {
		try {
			MimeMessage message = mailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message);

			helper.setSubject(level + " for Approval Request : Request Info Id - " + requestInfo.getId());
			helper.setFrom(fromAddress);
			helper.setTo(fromAddress);

			helper.setText("<p>There is a " + level + " for approval in your inbox.<p>"
					+ "Please login to the Service Portal to approve the request with Request ID " + requestInfo.getId()
					+ "</a><p>Thank you,</p>", html);

			mailSender.send(message);
		} catch (Exception e) {
			log.error(":: Error in sendEscalationMail() ::", e);
		}
	}
}
