package com.mo.service;

import com.mo.domain.UserRole;
import com.mo.requestDTO.LoginRequest;
import com.mo.response.AuthResponse;

import jakarta.mail.MessagingException;

public interface AuthService {

	public boolean isUserEmailExists(String email, UserRole role);
	
	public boolean isUserAddharNumberExist(String aadharNumber, UserRole role);
	
	public boolean sendVerificationOtp(String email, UserRole role) throws MessagingException;
	
	public void setAccountVerified(String email, String otp);
	
	public AuthResponse userSigin(LoginRequest request);
	public AuthResponse hostSigin(LoginRequest request);
}
