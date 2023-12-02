package com.sr.utility;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

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
	public void sendHTMLMail(Long requestInfoId) {
		try {
			MimeMessage message = mailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message);

			helper.setSubject("Approval Request : Request Info Id - " + requestInfoId);
			helper.setFrom(fromAddress);
			helper.setTo(fromAddress);

			helper.setText("<b>Hey guys</b>,<br><i>Welcome to my new home</i>", html);

			mailSender.send(message);
		} catch (Exception e) {
			log.error(":: Error in sendHTMLMail() ::", e);			
		}
	}
}