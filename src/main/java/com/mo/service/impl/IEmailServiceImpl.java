package com.mo.service.impl;

import org.springframework.mail.MailException;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.mo.service.IEmailService;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class IEmailServiceImpl implements IEmailService {

	private final JavaMailSender javaMailSender;

	@Override
	public void sendVerificationOtpEmail(String email, String subject, String text) throws MessagingException {

		try {
			MimeMessage mimeMessage = javaMailSender.createMimeMessage();
			MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, "utf-8");
			messageHelper.setSubject(subject);
			messageHelper.setText(text);
			messageHelper.setTo(email);

			javaMailSender.send(mimeMessage);

		} catch (MailException e) {

			throw new MailSendException("Failed to send a email", e);
		}

	}

	@Override
	public void sendComplaintRaisedEmail(String propertyEmail, String userEmail, String text, String subject)
			throws MessagingException {
		try {
			MimeMessage mimeMessage = javaMailSender.createMimeMessage();
			MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, "utf-8");
			messageHelper.setCc(userEmail);
			messageHelper.setSubject(subject);
			messageHelper.setText(text);
			messageHelper.setTo(propertyEmail);

			javaMailSender.send(mimeMessage);
		} catch (MailException e) {

			throw new MailSendException("Failed to send a email");
		}
	}

}
