package com.mo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mo.model.VerificationCode;

public interface VerificationCodeRepository extends JpaRepository<VerificationCode, Long> {

	public VerificationCode findByEmail(String email);

	public VerificationCode findByOtpCode(String otp);

}
