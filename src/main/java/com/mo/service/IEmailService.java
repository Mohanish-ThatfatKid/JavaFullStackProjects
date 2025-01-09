package com.mo.service;

import jakarta.mail.MessagingException;

public interface IEmailService {

	public void sendVerificationOtpEmail(String email, String subject, String text ) throws MessagingException;
	public void sendComplaintRaisedEmail(String propertyEmail, String userEmail, String text, String subject)
			throws MessagingException;
}
