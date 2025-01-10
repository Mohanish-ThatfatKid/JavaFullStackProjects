package com.mo.requestDTO;

import lombok.Data;

@Data
public class VerificationRequest {

	private String email;
	private String otp;
}
